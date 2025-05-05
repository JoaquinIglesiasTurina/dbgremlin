import ListDependent._
import org.scalamock.stubs.Stubs

class ListDependentSuite extends munit.FunSuite, Stubs:
  test("list dependent"):
    val mockWorkspaceClient = MockWorkspaceClient()
    listDependent(4321L, mockWorkspaceClient)
    assertEquals(mockWorkspaceClient.jobCalls, 2)