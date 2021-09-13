# Java Test

## JUnit Test

```@Beforeclass```： 在类中只会被执行一次，即在所有测试用例执行前才执行一次

```@Before```：在跑每一个测试方法@Test前都会执行一次

```@Test```: 具体的测试方法

```@After```：释放资源，在跑每一个测试方法@Test后都会执行一次

```@Afterclass```: 在类中只会被执行一次，即在所有测试用例执行完才执行一次

- 一个JUnit4的单元测试用例执行顺序为：   
```@BeforeClass -> @Before -> @Test -> @After -> @AfterClass; ```

- 每一个测试方法的调用顺序为：   
```@Before -> @Test -> @After; ```


