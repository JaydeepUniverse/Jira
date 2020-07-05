import com.atlassian.jira.component.ComponentAccessor
import com.opensymphony.workflow.InvalidInputException


log.warn "=== Script Started ==="

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def repoName = customFieldManager.getCustomFieldObjectByName("Git repositories")
def pipelinename = customFieldManager.getCustomFieldObjectByName("CI BuildJob")
log.warn pipelinename
log.warn pipelinename.getClass()
def techstackname = customFieldManager.getCustomFieldObjectByName("Tech Stack")
log.warn techstackname
log.warn techstackname.getClass()
def repoNameValue = issue.getCustomFieldValue(repoName)?.value
log.warn "repoNameValue: "+repoNameValue
def piplelineNamevalue = issue.getCustomFieldValue(pipelinename)?.value
log.warn "piplelineNamevalue: "+piplelineNamevalue
log.warn piplelineNamevalue.getClass()
def techstackNameValue = issue.getCustomFieldValue(techstackname)?.value
log.warn techstackNameValue.getClass()
log.warn "techstackNameValue: "+techstackNameValue
def BUILD_TOKEN = "xxxx"
log.warn "BUILD_TOKEN: "+BUILD_TOKEN
def urlConnection = new URL("http://x.x.x.x:8080/job/DSL_Demo_Seed/buildWithParameters?token=${BUILD_TOKEN}&Gitrepositories=${repoNameValue}&techstack=${techstackNameValue}&name_job_create=${piplelineNamevalue}").openConnection()
log.warn "urlConnection: "+urlConnection
def message = "{\"name\": \"${piplelineNamevalue}\", \"description\": \"This is your first pipleline\"}"
log.warn "message: "+message
def authString = "admin:xxxx".getBytes().encodeBase64().toString()
log.warn "authString: "+authString


def command = "curl -XGET \"http://x.x.x.x:8080/checkJobName?value=${piplelineNamevalue}\" --user admin:${BUILD_TOKEN}"
log.warn "command: "+command
Process process = command.execute()
log.warn "process: "+process
def out = new StringBuffer()
log.warn "out: "+out
def err = new StringBuffer()
log.warn "err: "+err
process.consumeProcessOutput( out, err )
process.waitFor()
log.warn "out: "+out
log.warn "err: "+err
log.warn "out size: "+out.size()


if (!piplelineNamevalue) {
    invalidInputException = new InvalidInputException("PipelineName Is Not Provided")
    def block4 = "block - PipelineName Is Not Provided"
    log.warn block4
} else if (!techstackNameValue) {
    invalidInputException = new InvalidInputException("Either TechStackName Is Not Provided")
    def block4 = "block - TechStackName Is Not Provided"
    log.warn block4
} else if( out.size() == 7 ) {
        def block1 = "In out size if block"
        log.warn block1
        urlConnection.setDoOutput(true)
        urlConnection.setRequestMethod("POST")
        urlConnection.setRequestProperty("Authorization", "Basic ${authString}")
        urlConnection.setRequestProperty("Content-Type", "application/json")
        def postRC = urlConnection.getResponseCode();

        println(urlConnection.getInputStream().getText())
        if(postRC.equals(201)) {
            println(post.getInputStream().getText());      
        } else {
            invalidInputException = new InvalidInputException("Jenkins Pipeline Creation failed.")
        }
    } 
    else{
        def block2 = "In out size else block, Jenkins Job Already Exist"
        log.warn block2
        invalidInputException = new InvalidInputException("Jenkins Job Already Exist")
    }
log.warn "=== Script Ended ==="
