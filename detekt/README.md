instructions:
---- DETEKT ----
- project level build.gradle.kts
plugins{
    id("dev.detekt") version "2.0.0-alpha.6" apply false
}

- other configs always in project level build.gradle
tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        checkstyle.required.set(true) // checkstyle(xml) like format mainly for integrations like Jenkins
        sarif.required.set(true) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to
        support integrations with GitHub Code Scanning
        markdown.required.set(true) // simple Markdown format
    }
}
// Kotlin DSL
tasks.withType<Detekt>().configureEach {
    jvmTarget = "1.8"
}
tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = "1.8"
}

- ( sync frequently )

- :app or other :module build.gradle.kts
plugins{
    id("dev.detekt")
}
detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("../config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("../config/baseline.xml") // a way of suppressing issues before introducing detekt
//  -dafault was
//  config.setFrom("$projectDir/config/detekt.yml") 
//  baseline = file("$projectDir/config/baseline.xml") 
}
dependencies {
  implementation("dev.detekt:detekt-api:2.0.0-alpha.6")
  detektPlugins("dev.detekt:detekt-rules-ktlint-wrapper:2.0.0-alpha.6")
}

- terminal command to create the config/detekt.yml
./gradlew detektGenerateConfig

- generate detekt report in the formats listed above
- the files are then created in :the_module build/reports/detekt -> html, md, sarif, xml
./gradlew detekt

- rules can be then adjusted as preferred in the config/detekt.yml file (root)
  # EXCLUDE RULES FOR COMPOSE
  # those configurations are advised from: https://detekt.dev/docs/introduction/compose/

  # FunctionNaming -> Set ignoreAnnotated to ['Composable']
  # TopLevelPropertyNaming -> Set constantPattern to '[A-Z][A-Za-z0-9]*' -> val FooPadding = 16.dp
  # LongParameterList -> Set functionThreshold to a higher value, ignoreDefaultParameters = true -> Composables may boast more than the typical number of function arguments
  # MagicNumber -> ignorePropertyDeclaration = true, good, is already default -> may be considered violations if they don't specify the named parameter (i.e. Color(color = 0xFFEA6D7E))
  # UnusedPrivateFunction -> Set ignoreAnnotated to include Preview ['Preview', 'PreviewLightDark', 'AppPreviews']
  # TooManyFunctions -> Set ignoreAnnotatedFunctions to ['Preview'] -> detekt may flag files with many composable preview functions

  # some others are just filling the report
  # FinalNewline -> many files in a newly created project have no final new line
  # NewLineAtEndOfFile -> many files are at default without
  # ImportOrdering -> lexicographic order not needed
  # TrailingCommaOnCallSite -> the last parameter can also not have a comma, that is going to be noticed from the developer that has to add some new parameters
  # BlankLineBetweenWhenConditions -> when() conditions can yet be more compact

---- LINT ----
- there is already one preinstalled in android studio,
- is this that highlights errors, like with quiggly lines, blends unused imports, gives hints and so on.
- a report can be generated with that as well via terminal, stored in :modules build/reports/ html, txt, sarif, xml
./gradlew lint 

---- KTLINT ----
- not that good, as detekt...
- reports after a little amount of "error" or code "bad formatting" just arrives until a certain point
- meaning that first are logged - created reports of the first "errors" "bad formatting"
- just when those are fixed, or is created a baseline then are scanned also other files.
- or also running many consecutive times the command, after a while it prints all issues (└─# ./gradlew ktlintCheck ,in this case)
- at least with "this version". if the plugin is applied only to :module then is scanned the whole,
- when the plugin is installed in :app and also :module, ktlint prints until a certain point, hence :module is not even reached
- to me is been a very bad time, i dont use ai for a task until i cannot know how to do it myself, to fix the "errors" can be a good task for ai, or for the autocorrect feature


- project level build.gradle.kts
plugins{ 
    id("org.jlleitschuh.gradle.ktlint") version "12.3.0"
}
- same for :module_s plugins{}  
- :modules build.gradle.kts -> output configuration

ktlint {
    version = "1.8.0"
    android = true
    ignoreFailures = false
    reporters {
    reporter(ReporterType.PLAIN)   
    reporter(ReporterType.HTML)
    reporter(ReporterType.JSON)
    reporter(ReporterType.CHECKSTYLE)
    reporter(ReporterType.SARIF)
  }
  filter {
    exclude("**/generated/**")
    exclude("**/build/**")
  }
}
- so is not just a txt report

- in the documentation at: https://github.com/JLLeitschuh/ktlint-gradle
- that applies the plugin to all modules
subprojects {
  apply(plugin = "org.jlleitschuh.gradle.ktlint") // Version should be inherited from parent

  // Optionally configure plugin
  configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
      debug.set(true)
  }
}


- create report, in :moduleswithplugin/build/reports/ktlint
./gradlew ktlintCheck

- generate base line, (saves all the previous "errors" "lints", as were solved)
- in :thatmodule/config/ktlint/ baseline.xml
./gradlew ktlintGenerateBaseLine

- also can be added an .editorconfig file to the root of your project to customize rules.
- For example, to allow wildcard imports and disable trailing comma enforcement:
  [*.{kt,kts}]
  ij_kotlin_imports_layout = *
  ktlint_standard_trailing-comma-on-call-site = disabled
  ktlint_standard_trailing-comma-on-declaration-site = disabled

- also useful due to differences
  [*.{kt,kts}]
  ktlint_code_style = android_studio

- fix formatting issues automatically
./gradlew ktlintFormat

- also in the :module build.gradle.kts can be instructed to format the code every time that the app is built
tasks.getByPath("preBuild").dependsOn("ktlintFormat")


