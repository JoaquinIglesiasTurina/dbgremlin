package GivePermissions
import com.databricks.sdk.WorkspaceClient
import com.databricks.sdk.service.jobs.Job
import com.databricks.sdk.service.jobs.JobAccessControlRequest
import com.databricks.sdk.service.jobs.JobPermissionLevel
import com.databricks.sdk.service.jobs.JobPermissionsRequest

import scala.jdk.CollectionConverters._

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
  permissionLevel: JobPermissionLevel,
  workspaceClient: WorkspaceClient
): Unit =
  val jobAccessControlRequest = new JobAccessControlRequest()
  jobAccessControlRequest.setPermissionLevel(permissionLevel)
  jobAccessControlRequest.setUserName(userEmail)

  val job = workspaceClient.jobs().get(jobId)
  DatabricksUtils
    .getAllDependentJobs(workspaceClient, job)
    .foreach(setPermissions(workspaceClient, jobAccessControlRequest, _))