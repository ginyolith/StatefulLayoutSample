package com.ginyolith.statefullayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        startLoadingButton.setOnClickListener {
            doSomethingSlow()
        }

        statefulLayout.onRetry = {
            doSomethingSlow()
        }
    }

    private fun doSomethingSlow() {
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
}
