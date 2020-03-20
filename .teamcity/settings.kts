import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2019.1"

project {

    vcsRoot(HttpsGithubComGopinathshivaAngularCliAppRefsHeadsMaster)

    buildType(id02ChromeTesting)
    buildType(id02PhantomJSTesting)
    buildType(id03Build)
    buildType(id01Lint)
}

object id01Lint : BuildType({
    id("01Lint")
    name = "01. Lint"

    vcs {
        root(HttpsGithubComGopinathshivaAngularCliAppRefsHeadsMaster)
    }

    steps {
        script {
            name = "Run lint"
            scriptContent = """
                npm install
                npm run lint
            """.trimIndent()
        }
    }

    triggers {
        vcs {
        }
    }
})

object id02ChromeTesting : BuildType({
    id("02ChromeTesting")
    name = "02. Chrome Testing"

    vcs {
        root(HttpsGithubComGopinathshivaAngularCliAppRefsHeadsMaster)
    }

    steps {
        script {
            scriptContent = """
                npm install
                npm run test-chrome
            """.trimIndent()
        }
    }

    triggers {
        vcs {
        }
    }

    dependencies {
        snapshot(id01Lint) {
        }
    }
})

object id02PhantomJSTesting : BuildType({
    id("02PhantomJSTesting")
    name = "02. PhantomJS Testing"

    vcs {
        root(HttpsGithubComGopinathshivaAngularCliAppRefsHeadsMaster)
    }

    steps {
        script {
            scriptContent = """
                npm install
                npm run test-phantomjs
            """.trimIndent()
        }
    }

    triggers {
        vcs {
        }
    }

    dependencies {
        snapshot(id01Lint) {
        }
    }
})

object id03Build : BuildType({
    id("03Build")
    name = "03. Build"

    vcs {
        root(HttpsGithubComGopinathshivaAngularCliAppRefsHeadsMaster)
    }

    steps {
        script {
            scriptContent = """
                npm install
                npm run build
            """.trimIndent()
        }
    }

    triggers {
        vcs {
        }
    }

    dependencies {
        snapshot(id02ChromeTesting) {
        }
        snapshot(id02PhantomJSTesting) {
        }
    }
})

object HttpsGithubComGopinathshivaAngularCliAppRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/gopinathshiva/angular-cli-app#refs/heads/master"
    url = "https://github.com/gopinathshiva/angular-cli-app"
})
