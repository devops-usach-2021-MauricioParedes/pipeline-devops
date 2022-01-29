def call(List<String> paramsAllowedStage){
		
		stage(STAGE_BUILD)
		{
			
			if (paramsAllowedStage.any{it== env.STAGE_NAME})
			{
			
				STAGE=env.STAGE_NAME
				sh 'env'
            			sh './gradlew clean build'
				println "Stage: ${env.STAGE_NAME}"
			}
			else
			{
				println '------- SKIPPED'+STAGE_BUILD+' ----------'
			}
		}
			
		stage(STAGE_SONAR){
			
			if (paramsAllowedStage.any{it== env.STAGE_NAME})
			{
				STAGE=env.STAGE_NAME
				def scannerHome = tool 'sonar-scanner';
				withSonarQubeEnv('sonar-server') {
				sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build -Dsonar.sources=src"
				}
			}
			else{
				println '------- SKIPPED'+STAGE_SONAR+' ----------'
			}
         	}
		
		stage(STAGE_RUN){
			
			if (paramsAllowedStage.any{it==env.STAGE_NAME})
			{
		    		STAGE=env.STAGE_NAME
				println "Stage: ${env.STAGE_NAME}"
            			sh "nohup bash gradlew bootRun & "
            			sleep 80
			}
			else
			{
				println '------- SKIPPED'+STAGE_RUN+' ----------'
			}
			
		}
	
		stage(STAGE_TEST){
			
			if (paramsAllowedStage.any{it==env.STAGE_NAME})
			{
				STAGE=env.STAGE_NAME
				println "Stage: ${env.STAGE_NAME}"
            			sh "curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
			}
			else
			{
				println '------- SKIPPED'+STAGE_TEST+' ----------'
			}
		}
	
		stage(STAGE_NEXUS) {
			
			if (paramsAllowedStage.any{it==env.STAGE_NAME})
			{
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
			else
			{
				println '------- SKIPPED'+STAGE_NEXUS+' ----------'
			}
            
        }
        
}
return this;

