package GivePermissions
import com.databricks.sdk.WorkspaceClient
import com.databricks.sdk.service.jobs.Job
import com.databricks.sdk.service.jobs.JobAccessControlRequest
import com.databricks.sdk.service.jobs.JobPermissionLevel
import com.databricks.sdk.service.jobs.JobPermissionsRequest

import scala.jdk.CollectionConverters._

val workspaceClient = new WorkspaceClient()

def setPermissions(
  workspaceClient: WorkspaceClient,
  jobAccessControlRequest: JobAccessControlRequest,
  job: Job
) =
  val collection = Seq(jobAccessControlRequest).asJavaCollection
  val permissions = new JobPermissionsRequest()
  permissions.setAccessControlList(collection)
  permissions.setJobId(job.getJobId().toString())
  workspaceClient.jobs().updatePermissions(permissions)

def givePermissions(
  userEmail: String,
  jobId: Long,
  permissionLevel: JobPermissionLevel = JobPermissionLevel.CAN_MANAGE_RUN,
  workspaceClient: WorkspaceClient = workspaceClient
): Unit =
  val jobAccessControlRequest = new JobAccessControlRequest()
  jobAccessControlRequest.setPermissionLevel(permissionLevel)
  jobAccessControlRequest.setUserName(userEmail)

  val job = workspaceClient.jobs().get(jobId)
  DatabricksUtils.getAllDependentJobs(workspaceClient, job)
    .foreach(setPermissions(workspaceClient, jobAccessControlRequest, _))