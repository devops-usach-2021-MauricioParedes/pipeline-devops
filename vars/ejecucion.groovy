
def call(){
 
   
 pipeline {

        agent any

        environment{
         STAGE=''
         STAGE_BUILD='buildAndTest'
         STAGE_SONAR='sonar'
         STAGE_RUN='runJar'
         STAGE_REST='rest'
         STAGE_NEXUSCI='nexusCI'
         
         STAGE_DOWNLOADNEXUS='downloadNexus'
         STAGE_RUNDOWNLOADEDJAR='runDownloadedJar'
         STAGE_NEXUSCD='nexusCD'
       }

        parameters {
                choice (choices: ['gradle', 'maven'], description: 'Indicar herramienta de contrucción', name: 'buildTool')
                string(name:'stage',defaultValue:'',description:'Write stages that you need execute or keep blank to execute all (example: build)')
        }

        stages{
                stage('Pipeline'){
                        steps{
                         script{

                    try{
                     figlet 'Pipeline'
                     
                     def ci_or_cd=verifyBranchName()
                     List<String> paramAllowed=getStageForExecution(params.stage,ci_or_cd)
                     
                                         
                     figlet params.buildTool
                     figlet 'Pipeline Type: '+ci_or_cd
                     
                     if(params.buildTool=='gradle'){
                          gradle.call(paramAllowed)

                     }
                     else{
                      
                          maven.call(paramAllowed)
                       

                     }
                     slackSend color: 'good', message: "[${env.USER}][${env.JOB_NAME}][${params.buildTool}] Ejecución exitosa"

                    }
                    catch(Exception e){
                        slackSend color: 'danger', message: "[${env.USER}][${env.JOB_NAME}][${params.buildTool}] Ejecución fallida en stage [${STAGE}]"
                                                error "Ejecución fallida en stage ${STAGE}"
                    }
                                }
                        }

                }

    }
} 
 
    

}

def verifyBranchName(){
  //def is_ci_or_cd=( env.GIT_BRANCH.contains('feature-'))?'CI':'CD'
  if(env.GIT_BRANCH.contains('feature-') || env.GIT_BRANCH.contains('develop')){
      return 'CI'
  }
  else{
      return 'CD'
  }


}

def getStageForExecution(String params,String ciOrCd){
 def stages=[]
 if(ciOrCd=='CI')
 {
   stages=[STAGE_BUILD,STAGE_SONAR,STAGE_RUN,STAGE_REST,STAGE_NEXUSCI]
 }
 else
 {
   if(ciOrCd=='CD')
   {
     stages=[STAGE_DOWNLOADNEXUS,STAGE_RUNDOWNLOADEDJAR,STAGE_REST,STAGE_NEXUSCD]
   }
   else
   {
      figlet "Wrong variable CI_OR_CD"
      error "Wrong variable CI_OR_CD ${ciOrCd}"
      return
    
   }
 }

 if(params=='')
 {
    return stages
 }
 else
 {
  def stagesSelected=params.split(';').toList()
  stagesSelected.each{ val-> 
    if(stages.any{it==val}==false)
    {
      figlet "stage not found"
      error "stage not found for ${ciOrCd}"
      return
    }
  }
    
  return stagesSelected
 }
  
 
}

return this;
