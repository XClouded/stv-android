/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.example.android.imagedownloader;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    private static final String[] URLS = {
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg",
        "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg"
       
        
    };
    /* "http://image1.webscache.com/baike/haibao/ipad/2011-12/202951452-147-2011-12-22-13-29-34.jpg",
        "http://image1.webscache.com/baike/haibao/ipad/2011-12/202951452-147-2011-12-22-13-29-34.jpg",
        "http://image1.webscache.com/baike/haibao/ipad/2011-10/202904018-147-2011-10-18-10-13-14.jpg",
        "http://image1.webscache.com/baike/haibao/ipad/2011-12/202895773-147-2011-12-16-16-38-03.jpg",
        "http://image1.webscache.com/baike/haibao/ipad/2011-11/202783113-147-2011-11-29-13-18-01.jpg",
        "http://image1.webscache.com/baike/haibao/ipad/2007-05/202543451-61-2007-05-18%2013-43-23.jpg",
        "http://image1.webscache.com/baike/haibao/ipad/2008-01/400093687-333-2008-01-30%2016-46-21.jpg",
        "http://image1.webscache.com/baike/haibao/ipad/2008-01/400093687-333-2008-01-30%2016-46-21.jpg",
        "http://image1.webscache.com/baike/haibao/ipad/2011-09/202912276-321-2011-09-19-17-15-23.jpg",
        "http://image1.webscache.com/baike/haibao/ipad/2011-09/202912276-321-2011-09-19-17-15-23.jpg",
        "http://image1.webscache.com/baike/haibao/ipad/2008-12/400008668-333-2008-12-26%2010-37-38.jpg",
        "http://image1.webscache.com/baike/haibao/ipad/2008-10/300000124-147-2008-10-28%2009-19-05.jpg",
        "http://image1.webscache.com/baike/haibao/ipad/2008-10/300000124-147-2008-10-28%2009-19-05.jpg",
        "http://image1.webscache.com/baike/haibao/ipad/2010-01/202657947-321-2010-01-08%2015-52-10.jpg"*/
    
    private final ImageDownloader imageDownloader = new ImageDownloader();
    
    public int getCount() {
        return URLS.length;
    }

    public String getItem(int position) {
        return URLS[position];
    }

    public long getItemId(int position) {
        return URLS[position].hashCode();
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = new ImageView(parent.getContext());
            view.setPadding(6, 6, 6, 6);
        }

        imageDownloader.download(URLS[position], (ImageView) view);
        
        return view;
    }

    public ImageDownloader getImageDownloader() {
        return imageDownloader;
    }
}
