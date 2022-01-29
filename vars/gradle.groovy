def call(List<String> paramsAllowedStage){
	
	paramsAllowedStage.each {
                        println 'elementogradle:'+it
          }
   	println '------- test 1 ----------'
	if (paramsAllowedStage.any{it=='build'})
	{
		println '------- test 2 ----------'
		stage('Build & Unit Test'){
			println '------- test 3 ----------'
				STAGE=env.STAGE_NAME
				println '------- test 4 ----------'
				sh 'env'
            			sh './gradlew clean build'
				println "Stage: ${env.STAGE_NAME}"
		}
			
	}
	else
	{
		println '------- SKIPPED build ----------'
	}
	
	stage('Sonar'){
            		STAGE=env.STAGE_NAME
            		def scannerHome = tool 'sonar-scanner';
            		withSonarQubeEnv('sonar-server') {
                	sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build -Dsonar.sources=src"
            		}
         	}
	stage('Run'){
		    	STAGE=env.STAGE_NAME
			    println "Stage: ${env.STAGE_NAME}"
            	sh "nohup bash gradlew bootRun & "
            	sleep 80
			
		}
	stage('Test'){
			STAGE=env.STAGE_NAME
			println "Stage: ${env.STAGE_NAME}"
            		sh "curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
	}
	stage('nexus') {
            		STAGE=env.STAGE_NAME
            		nexusPublisher nexusInstanceId: 'test-repo',
            		nexusRepositoryId: 'test-repo',
            		packages: [
                	[
                        	$class: 'MavenPackage',
                        	mavenAssetList: [
                            		[classifier: '', extension: '', filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']
                        	],
                        	mavenCoordinate: [
                            		artifactId: 'DevOpsUsach2020',
                            		groupId: 'com.devopsusach2020',
                            		packaging: 'jar',
                            		version: '0.0.1'
                        	]
                	]
            		]
            
        }
        
}
return this;

