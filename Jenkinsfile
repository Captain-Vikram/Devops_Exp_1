pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'testing', url: 'https://github.com/Captain-Vikram/Devops_Exp_1.git'
            }
        }
        stage('Find POM File') {
            steps {
                // This command searches all folders for "pom.xml" and prints the location
                bat 'dir /s /b pom.xml'
            }
        }
    }
}
