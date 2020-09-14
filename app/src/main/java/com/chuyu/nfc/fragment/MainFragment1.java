package com.chuyu.nfc.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuyu.nfc.R;
import com.chuyu.nfc.adapter.PhotoGridAdapter;
import com.chuyu.nfc.base.BaseFragment;
import com.chuyu.nfc.bean.company;
import com.chuyu.nfc.bean.nfcRecording;
import com.chuyu.nfc.cusview.NoScrollGridView;
import com.chuyu.nfc.tools.EventBus.EventCenter;
import com.chuyu.nfc.tools.EventBus.EventCode;
import com.chuyu.nfc.tools.ScreenUtil;
import com.chuyu.nfc.ui.MainActivity;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.PhotoPreviewActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class MainFragment1 extends BaseFragment {
    private LinearLayout layout1;
    private LinearLayout layout2;
    private TextView tvId;
    private NoScrollGridView gridLayout;

    private EditText edit;
    private ImageView quxiao;
    private ImageView queren;

    public static MainFragment1 newInstance() {
        Bundle args = new Bundle();
        MainFragment1 fragment = new MainFragment1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean isRegisterEventBusHere() {
        return true;
    }

    @Override
    protected void eventBusResult(EventCenter eventCenter) {
        switch (eventCenter.getEventCode()) {
            case EventCode.CODE1:
                String nfcid = (String) eventCenter.getData();
                Log.e("abc",nfcid);
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
                company company = MainActivity.uid.get(nfcid);
                tvId.setText(company.getUname() + "--" + company.getPos());
                break;
        }
    }

    @Override
    protected void initView() {
        layout1 = rootView.findViewById(R.id.layout1);
        layout2 = rootView.findViewById(R.id.layout2);
        tvId = rootView.findViewById(R.id.tv_id);
        gridLayout = rootView.findViewById(R.id.grid_layout);
        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.GONE);
        edit = rootView.findViewById(R.id.edit);
        quxiao = rootView.findViewById(R.id.quxiao);
        queren = rootView.findViewById(R.id.queren);
        quxiao.setOnClickListener(this);
        queren.setOnClickListener(this);
        setGridView();
    }


    //网络数据
    @Override
    protected void initDatas() {


    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_main_home_page;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quxiao:
                showtoast("取消");
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                break;
            case R.id.queren:
                if (imagePaths.contains("000000")) {
                    imagePaths.remove("000000");
                }
                String trim = edit.getText().toString().trim();
                String s = "";
                for (String imagePath : imagePaths) {
                    s += imagePath + ",";
                }
                nfcRecording a = new nfcRecording(tvId.getText().toString().trim(), s, trim);
                int b = 0;
                for (nfcRecording nfcRecording : MainActivity.v1) {
                    if (nfcRecording.getName().equals(tvId.getText().toString().trim())) {
                        b = 1;
                    }
                }
                if (b != 1) {
                    MainActivity.v1.add(a);
                }
                showtoast("确认");
                imagePaths.clear();
                imagePaths.add("000000");
                gridAdapter.notifyDataSetChanged();
                gridAdapter = new PhotoGridAdapter(imagePaths, imgsize, imgwidth, getActivity());
                gridLayout.setAdapter(gridAdapter);
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                break;

        }
    }

    private int imgsize = 9;//最多显示几张图片
    private ArrayList<String> imagePaths = new ArrayList<>();
    private final int REQUEST_CAMERA_CODE = 10;
    private final int REQUEST_PREVIEW_CODE = 20;
    private int imgwidth;
    private PhotoGridAdapter gridAdapter;

    private void setGridView() {
        imgwidth = (ScreenUtil.getScreenWidth(getActivity()) - ScreenUtil.dp2px(getActivity(), 46)) / 4;
        gridLayout.setNumColumns(4);
        gridLayout.setVerticalSpacing(5);//行间距
        gridLayout.setHorizontalSpacing(0);
        gridLayout.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 选择图片和预览
        gridLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgs = (String) parent.getItemAtPosition(position);
                if ("000000".equals(imgs)) {
                    checkcamera();
                } else {
                    //预览图片
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(getActivity());
                    intent.setCurrentItem(position);
                    intent.setPhotoPaths(imagePaths);
                    startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                }
            }
        });

        if (imagePaths.size() < imgsize - 1) {//如果照片集合达到9张 就不添加相机按钮
            imagePaths.add(0, "000000");
        }
        gridAdapter = new PhotoGridAdapter(imagePaths, imgsize, imgwidth, getActivity());
        gridLayout.setAdapter(gridAdapter);

    }

    //图片返回回来 重新刷新adapter
    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths != null && imagePaths.size() > 0) {
            imagePaths.clear();
        }
        //把添加按钮移除，再把按钮添加到集合末尾
        if (paths.contains("000000")) {
            paths.remove("000000");
        }
        paths.add("000000");
        imagePaths.addAll(paths);
        gridAdapter.notifyDataSetChanged();
        gridAdapter = new PhotoGridAdapter(imagePaths, imgsize, imgwidth, getActivity());
        gridLayout.setAdapter(gridAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    loadAdpater(list);
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    loadAdpater(ListExtra);
                    break;
            }
        }
    }


    private void checkcamera() {
        if (XXPermissions.isHasPermission(getActivity(), Permission.WRITE_EXTERNAL_STORAGE)) {
            PhotoPickerIntent intent = new PhotoPickerIntent(getActivity());
            intent.setSelectModel(SelectModel.MULTI);
            intent.setShowCarema(true);
            intent.setMaxTotal(imgsize);
            intent.setSelectedPaths(imagePaths);
            startActivityForResult(intent, REQUEST_CAMERA_CODE);
        } else {
            XXPermissions.with(getActivity())
                    .permission(Permission.Group.STORAGE)
                    .request(new OnPermission() {

                        @Override
                        public void hasPermission(List<String> granted, boolean isAll) {
                            PhotoPickerIntent intent = new PhotoPickerIntent(getActivity());
                            intent.setSelectModel(SelectModel.MULTI);
                            intent.setShowCarema(false);
                            intent.setMaxTotal(imgsize);
                            intent.setSelectedPaths(imagePaths);
                            startActivityForResult(intent, REQUEST_CAMERA_CODE);
                        }

                        @Override
                        public void noPermission(List<String> denied, boolean quick) {
                            showtoast("请手动打开读取相片权限");
                        }
                    });
        }
    }
}
