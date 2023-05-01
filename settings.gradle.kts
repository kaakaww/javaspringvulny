rootProject.name = "JavaSpringVulny"
include("hawkscripts")

rootProject.children.forEach { projectDescriptor ->
    projectDescriptor.buildFileName = "${projectDescriptor.name}.gradle.kts"
}
