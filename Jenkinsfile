pipeline {
    agent any
    
    stages {
        // Stage 1: Get the code from GitHub
        stage('Checkout Code') {
            steps {
                // I've set the branch to 'main'. If your repo uses 'master', change 'main' to 'master' below.
                git branch: 'main', url: 'https://github.com/Captain-Vikram/Devops_Exp_1.git'
            }
        }

        // Stage 2: Scan the downloaded code
        stage('SonarQube Analysis') {
            steps {
                script {
                    // Tool name must match Global Tool Configuration (Check casing!)
                    def scannerHome = tool name: 'SonarScanner', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
                    
                    // Server name must match System Configuration
                    withSonarQubeEnv('SonarServer') {
                        // 1. We use the projectKey 'Devops_Exp_1' (you can name this anything unique)
                        // 2. We use 'sources=.' to scan the current directory we just checked out
                        bat "\"${scannerHome}\\bin\\sonar-scanner.bat\" -Dsonar.projectKey=Devops_Exp_1 -Dsonar.sources=."
                    }
                }
            }
        }
    }
}
