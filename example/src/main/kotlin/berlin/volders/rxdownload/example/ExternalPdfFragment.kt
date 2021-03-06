/*
 * Copyright (C) 2017 volders GmbH with <3 in Berlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package berlin.volders.rxdownload.example

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import kotlinx.android.synthetic.main.fragment_base.*

class ExternalPdfFragment : PageFragment("rxdm-external.pdf") {

    override fun getStubViewLayout() = R.layout.fragment_external_pdf

    override fun getUri() = Uri.parse(getString(R.string.pdf_download_url))

    override fun onDownloadCompleted(uri: Uri) {
        progressBar.visibility = View.GONE
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
}
