package com.ticket.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ticket.R;
import com.ticket.bean.PicturesVo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果；
 * 既支持自动轮播页面也支持手势滑动切换页面
 */
public class SlideShowView extends FrameLayout implements View.OnClickListener {

    // 使用universal-image-loader插件读取网络图片，需要工程导入universal-image-loader-1.8.6-with-sources.jar
    private ImageLoader imageLoader = ImageLoader.getInstance();

    private static final String TAG = SlideShowView.class.getSimpleName();

    //轮播图图片数量
    private final static int IMAGE_COUNT = 5;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 5;
    //自动轮播启用开关
    private boolean isAutoPlay;

    //自定义轮播图的资源
    private List<PicturesVo> imageUrls = new ArrayList<PicturesVo>();
    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList;
    //放圆点的View的list
    private List<View> dotViewsList;

    private ViewPager viewPager;
    //当前轮播页
    private int currentItem = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;

    private Context context;

    private boolean isInited = false;

    private OnImageClickedListener mListener;
    private OnImageSelectedListener onImageSelectedListener;

    public class ImageViewTag {
        public ImageViewTag(int position, String url, String navUrl) {
            this.position = position;
            this.url = url;
            this.navUrl = navUrl;
        }

        public int position;
        public String url;
        public String navUrl;

        public int getPosition() {
            return position;
        }

        public String getUrl() {
            return url;
        }

        public String getNavUrl() {
            return navUrl;
        }
    }

    @Override
    public void onClick(View v) {
        final ImageViewTag tag = (ImageViewTag) v.getTag();
        if (null != mListener) {
            this.mListener.onImageClicked(tag.position, tag);
        }
    }

    public interface OnImageClickedListener {
        public void onImageClicked(int position, ImageViewTag imageViewTag);
    }

    public interface OnImageSelectedListener {
        public void onImageSelected(int position);
    }

    public void setOnImageSelectedListener(OnImageSelectedListener onImageSelectedListener) {
        this.onImageSelectedListener = onImageSelectedListener;
    }

    public void setOnImageClickedListener(OnImageClickedListener listener) {
        this.mListener = listener;
    }

    //Handler
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }
    };

    public SlideShowView(Context context) {
        this(context, null);
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlideFigure);
        isAutoPlay = typedArray.getBoolean(R.styleable.SlideFigure_autoPlay, true);
        typedArray.recycle();
        initImageLoader(context);
        initData();
        if (isAutoPlay) {
            startPlay();
        }
    }

    /**
     * 开始轮播图切换
     */
    private synchronized void startPlay() {
        if (scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 4, 4, TimeUnit.SECONDS);
        }
    }

    /**
     * 停止轮播图切换
     */
    private synchronized void stopPlay() {
        scheduledExecutorService.shutdown();
    }

    /**
     * 初始化相关Data
     */
    private void initData() {
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();
    }

    /**
     * 初始化Views等UI
     */
    private synchronized void initUI(Context context) {
        if (imageUrls == null || imageUrls.size() == 0)
            return;

        if (!isInited) {
            LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);
            isInited = true;
        }
        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();
        dotViewsList.removeAll(dotViewsList);
        imageViewsList.removeAll(imageViewsList);

        // 热点个数与图片特殊相等
        for (int i = 0; i < imageUrls.size(); i++) {
            ImageView view = new ImageView(context);
            view.setTag(new ImageViewTag(i, imageUrls.get(i).getPictureUrl(), imageUrls.get(i).getNavigateUrl()));
            if (i == 0)//给一个默认图
                view.setBackgroundResource(R.drawable.no_banner);
            view.setScaleType(ScaleType.FIT_XY);
            imageViewsList.add(view);
            view.setOnClickListener(this);

            ImageView dotView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;
            params.rightMargin = 4;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);
        getDotViewsList(0);
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.addOnPageChangeListener(new MyPageChangeListener());
    }

    /**
     * 填充ViewPager的页面适配器
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //((ViewPag.er)container).removeView((View)object);
            container.removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViewsList.get(position);

            DisplayImageOptions options = new DisplayImageOptions
                    .Builder()
                    .showImageForEmptyUri(R.drawable.no_banner)
                    .showImageOnFail(R.drawable.no_banner)
                    .showImageOnLoading(R.drawable.no_banner)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            imageLoader.displayImage(((ImageViewTag) imageView.getTag()).url + "", imageView, options);
//            imageLoader.displayImage(((ImageViewTag)imageView.getTag()).url + "", imageView);
            container.addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements OnPageChangeListener {

        boolean autoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    if (isAutoPlay) {
                        stopPlay();
                    }
                    autoPlay = false;
                    break;
                case 2:// 界面切换中
                    autoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (isAutoPlay) {
                        startPlay();
                    }
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !autoPlay) {
                        viewPager.setCurrentItem(0, false);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0 && !autoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1, false);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int pos) {
            currentItem = pos;
            //选择的回调函数
            if (onImageSelectedListener != null) {
                onImageSelectedListener.onImageSelected(currentItem);
            }
            //ILogger.d(TAG, pos + "");
            getDotViewsList(pos);
        }

    }

    private void getDotViewsList(int pos) {
        for (int i = 0; i < dotViewsList.size(); i++) {
            if (i == pos) {
                ((View) dotViewsList.get(pos)).setBackgroundResource(R.drawable.dot_focus);
            } else {
                ((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.dot_blur);
            }
        }
    }

    /**
     * 执行轮播图切换任务
     */
    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }

    /**
     * 销毁ImageView资源，回收内存
     */
    private void destoryBitmaps() {

        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                //解除drawable对view的引用
                drawable.setCallback(null);
            }
        }
    }

    public void setImageUrlList(List<PicturesVo> urlList) {
        imageUrls = urlList;
        initUI(context);
    }

    public void addImageUrl(PicturesVo picturesVo) {
        if (!imageUrls.contains(picturesVo)) {
            imageUrls.add(picturesVo);
            initUI(context);
        }
    }

    public void clearImages() {
        imageUrls.clear();
        initUI(context);
    }

    /**
     * ImageLoader 图片组件初始化
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .discCacheFileNameGenerator(new Md5FileNameGenerator())
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
//                    .writeDebugLogs() // Remove for release app
                    .build();
            // Initialize ImageLoader with configuration.
            ImageLoader.getInstance().init(config);
        }
    }

    public void onDestroy() {
        stopPlay();
    }
}
