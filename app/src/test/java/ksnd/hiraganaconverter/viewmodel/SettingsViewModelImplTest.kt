package ksnd.hiraganaconverter.viewmodel

//@OptIn(ExperimentalCoroutinesApi::class)
//@RunWith(RobolectricTestRunner::class)
//class SettingsViewModelImplTest {
//    private val context = ApplicationProvider.getApplicationContext<Context>()
//    private val testDispatcher = UnconfinedTestDispatcher()
//    private val dataStore = PreferenceDataStoreFactory.create(
//        produceFile = { context.preferencesDataStoreFile("TestDataStore") },
//    )
//    private val viewModel = SettingsViewModelImpl(
//        dataStoreRepository = DataStoreRepositoryImpl(dataStore, ioDispatcher = testDispatcher),
//        ioDispatcher = testDispatcher,
//    )
//
//    @Before
//    fun setUp() {
//        Dispatchers.setMain(testDispatcher)
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun settingViewModel_Initialization_FirstDefaultSet() = runTest {
//        // モードの初期設定は自動モード
//        assertThat(viewModel.isSelectedThemeNum(ThemeNum.AUTO.num)).isTrue()
//        // フォントの初期設定はDefault
//        assertThat(viewModel.isSelectedFont(CustomFont.DEFAULT)).isTrue()
//    }
//
//    @Test
//    fun settingViewModel_SettingNightMode_NotAuto() = runTest {
//        // ダークモードを設定したときにデフォルトでないことを確認
//        viewModel.updateThemeNum(ThemeNum.NIGHT.num)
//        assertThat(viewModel.isSelectedThemeNum(ThemeNum.NIGHT.num)).isTrue()
//        assertThat(viewModel.isSelectedThemeNum(ThemeNum.AUTO.num)).isFalse()
//    }
//
//    @Test
//    fun settingViewModel_SettingCustomFont_NotDefault() = runTest {
//        // カスタムフォントを設定したときにデフォルトでないことを確認
//        viewModel.updateCustomFont(CustomFont.BIZ_UDGOTHIC)
//        assertThat(viewModel.isSelectedFont(CustomFont.BIZ_UDGOTHIC)).isTrue()
//        assertThat(viewModel.isSelectedFont(CustomFont.DEFAULT)).isFalse()
//    }
//}
