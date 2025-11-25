import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.kotlin.dsl.libs

plugins {
    java
    `maven-publish`
    alias(libs.plugins.loom)
}

println("TMM Patch v${"mod_version"()}")

val isRelease = System.getenv("RELEASE_BUILD")?.toBoolean() ?: false
val buildNumber = System.getenv("GITHUB_RUN_NUBMER")?.toInt()
val gitHash = "\"${calculateGitHash() + (if (hasUnstaged()) "-modified" else "")}\""
val accessWidenerFile = file("src/main/resources/tmm_patch.accesswidener")

base.archivesName.set("archives_base_name"())
group = "maven_group"()

val build = buildNumber?.let { "-build.${it}" } ?: "-local"

version = "${"mod_version"()}+mc${libs.versions.mc.get() + if (isRelease) "" else build}"

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = 21
}

repositories {
    mavenCentral()
    exclusiveMaven("https://api.modrinth.com/maven", "maven.modrinth")
    exclusiveMaven("https://maven.terraformersmc.com/releases/", "com.terraformersmc") // Mod Menu
    exclusiveMaven("https://maven.ladysnake.org/releases", "org.ladysnake.cardinal-components-api", "dev.doctor4t") // Cardinal Components, TMM
    exclusiveMaven("https://maven.uuid.gg/releases", "dev.upcraft.datasync") // Datasync
    exclusiveMaven("https://maven.maxhenkel.de/repository/public", "de.maxhenkel.voicechat") // Simple Voice Chat
    exclusiveMaven("https://maven.bawnorton.com/releases", "com.github.bawnorton.mixinsquared") // MixinSquared
    exclusiveMaven("https://maven.gegy.dev/releases", "dev.lambdaurora", "dev.lambdaurora.lambdynamiclights", "io.github.queerbric") // LambDynamicLights
}

val loom = project.extensions.getByType<LoomGradleExtensionAPI>()
loom.apply {
    runs.configureEach {
        vmArg("-XX:+AllowEnhancedClassRedefinition")
        vmArg("-XX:+IgnoreUnrecognizedVMOptions")
        vmArg("-Dmixin.debug.export=true")
    }
}

loom {
    accessWidenerPath = accessWidenerFile
}

dependencies {
    minecraft(libs.mc)
    mappings(variantOf(libs.yarn) { classifier("v2") })

    modImplementation(libs.fl)
    modImplementation(libs.fapi)

    modImplementation(libs.datasync)

    modImplementation(libs.cca.base)
    modImplementation(libs.cca.world)
    modImplementation(libs.cca.entity)
    modImplementation(libs.cca.scoreboard)

    modImplementation(libs.lambdynlights.api)
    modImplementation(libs.lambdynlights.runtime)

    modImplementation(libs.trainmurdermystery)

    // Development QOL
    modLocalRuntime(libs.modmenu)

    // MixinSquared
    include(implementation(annotationProcessor(libs.mixinsquared.get())!!)!!)
}

tasks.processResources {
    val properties = mapOf(
        "version" to version,
        "minecraft_version" to libs.versions.mc.get(),
        "fabric_api_version" to libs.versions.fapi.get(),
        "fabric_loader_version" to libs.versions.fl.get(),
    )

    inputs.properties(properties)

    filesMatching("fabric.mod.json") {
        expand(properties)
    }

    // don't add development or to-do files into built jar
    exclude("**/*.bbmodel", "**/*.lnk", "**/*.xcf", "**/*.md", "**/*.txt", "**/*.blend", "**/*.blend1")
}

tasks.jar {
    archiveClassifier = "dev"

    manifest {
        attributes(mapOf("Git-Hash" to gitHash))
    }
}

tasks.named<Jar>("sourcesJar") {
    manifest {
        attributes(mapOf("Git-Hash" to gitHash))
    }
}

operator fun String.invoke(): String {
    return rootProject.ext[this] as? String
        ?: throw IllegalStateException("Property $this is not defined")
}

fun calculateGitHash(): String {
    val stdout = providers.exec {
        commandLine("git", "rev-parse", "HEAD")
    }.standardOutput
    return stdout.asText.get().trim()
}

fun hasUnstaged(): Boolean {
    val stdout = providers.exec {
        commandLine("git", "status", "--porcelain")
    }.standardOutput
    val result = stdout.asText.get().replace("M gradlew", "").trimEnd()
    if (result.isNotEmpty())
        println("Found stageable results:\n${result}\n")
    return result.isNotEmpty();
}

fun RepositoryHandler.exclusiveMaven(url: String, vararg groups: String) {
    exclusiveContent {
        forRepository { maven(url) }
        filter {
            groups.forEach {
                includeGroup(it)
            }
        }
    }
}