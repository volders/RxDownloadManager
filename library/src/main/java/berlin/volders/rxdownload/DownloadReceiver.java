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

package berlin.volders.rxdownload;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.subjects.AsyncSubject;

class DownloadReceiver extends BroadcastReceiver implements Func0<Observable<Long>> {

    private final long downloadId;
    private final AsyncSubject<Long> receivedId;

    DownloadReceiver(long downloadId) {
        this.downloadId = downloadId;
        this.receivedId = AsyncSubject.create();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (downloadId == intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)) {
            context.unregisterReceiver(this);
            receivedId.onNext(downloadId);
            receivedId.onCompleted();
        }
    }

    @Override
    public Observable<Long> call() {
        Looper me = Looper.myLooper();
        if (me == null) {
            return Observable.from(receivedId.toBlocking().toFuture());
        }
        if (me == Looper.getMainLooper()) {
            return receivedId.asObservable();
        }
        return receivedId.observeOn(AndroidSchedulers.from(me));
    }
}
