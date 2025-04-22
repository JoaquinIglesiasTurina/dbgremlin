import com.databricks.sdk.WorkspaceClient
import com.databricks.sdk.service.jobs.Job
import com.databricks.sdk.service.jobs.JobAccessControlRequest
import com.databricks.sdk.service.jobs.JobPermissionLevel
import com.databricks.sdk.service.jobs.JobPermissionsRequest
import com.databricks.sdk.service.jobs.ListJobsRequest
import com.databricks.sdk.service.jobs.RunJobTask
import com.databricks.sdk.service.jobs.Task

import scala.jdk.CollectionConverters._

val workspaceClient = new WorkspaceClient()

def getJobIdFromTask(task: Task): Option[Long] =
  task.getRunJobTask() match
    case runJobTask: RunJobTask => Some(runJobTask.getJobId())
    case null => None

def getDependentJobIds(job: Job): List[Long] =
  job.getSettings().getTasks().asScala.toList
    .map(getJobIdFromTask)
    .filter(_.isDefined)
    .map(_.get)
    .distinct
  
def setPermissions(
  workspaceClient: WorkspaceClient,
  jobAccessControlRequest: JobAccessControlRequest,
  job: Job
) =
  val collection = Seq(jobAccessControlRequest).asJavaCollection
  val oldPermissions = workspaceClient.jobs().getPermissions(job.getJobId().toString())
  val permissions = new JobPermissionsRequest()
  permissions.setAccessControlList(collection)
  permissions.setJobId(job.getJobId().toString())
  workspaceClient.jobs().updatePermissions(permissions)

def givePermissions(
  userEmail: String,
  jobId: Long,
  workspaceClient: WorkspaceClient = workspaceClient
): Unit =
  val jobAccessControlRequest = new JobAccessControlRequest()
  jobAccessControlRequest.setPermissionLevel(JobPermissionLevel.CAN_MANAGE_RUN)
  jobAccessControlRequest.setUserName(userEmail)

  val job = workspaceClient.jobs().get(jobId)
  val tasks = job.getSettings().getTasks().asScala.toList
  var jobIds = tasks.map(getJobIdFromTask).filter(_.isDefined).map(_.get).distinct
  var outputJobs: List[Job] = List(job)
  while (jobIds.size > 0) {
    val j = workspaceClient.jobs().get(jobIds.head)
    val newJobs = job.getSettings().getTasks().asScala.toList
      .map(getJobIdFromTask).filter(_.isDefined).map(_.get).distinct
    outputJobs = outputJobs :+ j
    val outputJobIds = outputJobs.map(_.getJobId) 
    jobIds = (jobIds.tail ++ newJobs).distinct.filter(ji => !(outputJobIds.contains(ji)))
  }
  outputJobs.foreach(setPermissions(workspaceClient, jobAccessControlRequest, _))