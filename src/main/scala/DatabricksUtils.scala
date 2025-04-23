import com.databricks.sdk.WorkspaceClient
import com.databricks.sdk.service.jobs.Job
import com.databricks.sdk.service.jobs.RunJobTask
import com.databricks.sdk.service.jobs.Task

import scala.jdk.CollectionConverters._
import scala.util.Failure
import scala.util.Success
import scala.util.Try

def getJobIdFromTask(task: Task): Option[Long] =
  task.getRunJobTask() match
    case runJobTask: RunJobTask => Some(runJobTask.getJobId())
    case null => None

def getDependentJobIds(job: Job): Try[List[Long]] = Try {
  job.getSettings().getTasks().asScala.toList
    .map(getJobIdFromTask)
    .filter(_.isDefined)
    .map(_.get)
    .distinct
  }

def getAllDependentJobs(
  workspaceClient: WorkspaceClient,
  job: Job
): List[Job] = 
  var jobIds = getDependentJobIds(job).get
  var outputJobs: List[Job] = List(job)
  while (jobIds.size > 0)
    val j = workspaceClient.jobs().get(jobIds.head)
    outputJobs = outputJobs :+ j
    val outputJobIds = outputJobs.map(_.getJobId)
    
    getDependentJobIds(j) match 
      case Success(newJobs) => 
        jobIds = (jobIds.tail ++ newJobs)
          .distinct.filter(ji => !(outputJobIds.contains(ji)))
      case Failure(exception) => 
        new Exception("Could not get dependent jobs")
  
  outputJobs