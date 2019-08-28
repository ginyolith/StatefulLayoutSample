## 使い方

### layout.xmlでの使い方

- 下記のように、状態管理したいレイアウトの上にネストして、layout.xmlに定義する
- databindingを用いてstateの状態をlayoutに渡すことが可能。
- ScrollViewの実装と同じように、1つ以上のViewを追加しようとするとExceptionを投げる設計となっている。

```xml
<com.ginyolith.statefullayout.widget.StatefulLayout\
        ~~
        app:stateful_layout_error="@layout/view_error_common"
        app:stateful_layout_retry_button_id="@+id/retryButton"
        app:stateful_layout_loading="@layout/view_loading_common"
        app:state="@{viewModel.state}"
        tools:context=".MainActivity">

    <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            tools:context=".MainActivity">

            ~~

    </androidx.constraintlayout.widget.ConstraintLayout>

</com.ginyolith.statefullayout.widget.StatefulLayout>
```

#### attributes

| name | description |
|------|-------------|
|stateful_layout_error | エラー画面として表示するレイアウト |
|stateful_layout_retry_button_id | 上記layout.xmlに置いて、retryボタンとして定義されているViewのId |
|stateful_layout_loading| 読込中画面として表示するレイアウト |

### Controllerでの使い方

- `StatefulLayout.state` プロパティに `StatefulLayout.State` enumクラスを渡すと、対応する状態のViewに切り替わる
- StatefulLayout.Stateは下記3つのプロパティが定義されている
    - Error   : エラー画面を表示する
    - Loading : 読込中画面を表示する
    - Display : layout.xmlで定義した唯一の子Viewを表示する

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ~~
    }

    private fun doSomethingSlow() {
        val statefulLayout = findViewById(R.id.statefulLayout)
        statefulLayout.state = StatefulLayout.State.Loading
        val handler = Handler()
        thread {
            Thread.sleep(3000)

            handler.post {
                statefulLayout.state = if (Random.nextBoolean()) {
                    StatefulLayout.State.Display
                } else {
                    StatefulLayout.State.Error
                }
            }
        }
    }
```