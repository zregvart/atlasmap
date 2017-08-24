pipeline {

  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
  }

  agent {
    kubernetes {
      label 'datamaper-build'
      containerTemplate {
        name 'maven'
        image 'openjdk:8'
        ttyEnabled true
        command 'cat'
      }
    }
  }

  stages {
    stage ('Build') {
      steps {
        sh './mvnw -Dmaven.test.failure.ignore=true install' 
      }
      post {
        success {
          junit 'target/surefire-reports/**/*.xml' 
        }
      }
    }
  }

}
