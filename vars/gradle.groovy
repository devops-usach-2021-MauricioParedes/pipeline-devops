def call(List<String> paramsAllowedStage){

	if (paramsAllowedStage.any{it== STAGE_BUILD})
	{
		stage(STAGE_BUILD)
		{
			figlet STAGE_BUILD
			STAGE=env.STAGE_NAME
			sh 'env'
            		sh './gradlew clean build'
			println "Stage: ${env.STAGE_NAME}"
					
		}
	}
	else
	{
		println '------- SKIPPED '+STAGE_BUILD+' ----------'
	}
	if (paramsAllowedStage.any{it== STAGE_SONAR})
	{		
		stage(STAGE_SONAR){
			figlet STAGE_SONAR	
			STAGE=env.STAGE_NAME
			def scannerHome = tool 'sonar-scanner';
			withSonarQubeEnv('sonar-server') {
			sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build -Dsonar.sources=src"
			}
         	}
	}
	else
	{
		println '------- SKIPPED '+STAGE_SONAR+' ----------'
	}
	if (paramsAllowedStage.any{it== STAGE_RUN})
	{
		stage(STAGE_RUN){
			figlet STAGE_RUN
			STAGE=env.STAGE_NAME
			println "Stage: ${env.STAGE_NAME}"
            		sh "nohup bash gradlew bootRun & "
            		sleep 80
		}
						
	}
	else
	{
		println '------- SKIPPED '+STAGE_RUN+' ----------'
	}
	if (paramsAllowedStage.any{it==STAGE_REST})
	{
		stage(STAGE_REST){
			figlet STAGE_REST
			STAGE=env.STAGE_NAME
			println "Stage: ${env.STAGE_NAME}"
            		sh "curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
		}
			
	}
	else
	{
		println '------- SKIPPED '+STAGE_REST+' ----------'
	}
	if (paramsAllowedStage.any{it==STAGE_NEXUSCI})
	{
		stage(STAGE_NEXUSCI) {
			figlet STAGE_NEXUSCI
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
	else
	{
		println '------- SKIPPED '+STAGE_NEXUSCI+' ----------'
	}
	if (paramsAllowedStage.any{it==STAGE_DOWNLOADNEXUS})
	{
		stage(STAGE_DOWNLOADNEXUS) {
			figlet STAGE_DOWNLOADNEXUS
			sh "curl -X GET -u admin:majapafi20 http://localhost:8085/repository/test-repo/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O"		
			
			
		}
	}
	else
	{
		println '------- SKIPPED '+STAGE_DOWNLOADNEXUS+' ----------'
	}
	if (paramsAllowedStage.any{it==STAGE_RUNDOWNLOADEDJAR})
	{
		stage(STAGE_RUNDOWNLOADEDJAR) {
			figlet STAGE_RUNDOWNLOADEDJAR
			sh "nohup java -jar DevOpsUsach2020-0.0.1.jar &"
			sleep 60
			
		}
	}
	else
	{
		println '------- SKIPPED '+STAGE_RUNDOWNLOADEDJAR+' ----------'
	}
	
	if (paramsAllowedStage.any{it==STAGE_NEXUSCD})
	{
		stage(STAGE_NEXUSCD) {
			figlet STAGE_NEXUSCD
					
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
						version: '1.0.0'
					]
				]
				]
		}
	}
	else
	{
		println '------- SKIPPED '+STAGE_NEXUSCD+' ----------'
	}
            
        
        
}
return this;

