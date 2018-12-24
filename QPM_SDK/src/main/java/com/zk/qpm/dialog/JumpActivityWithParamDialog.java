package com.jm.android.gt.dialog;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jm.android.gt.R;
import com.jm.android.gt.adapter.CommonRecyclerAdapter;
import com.jm.android.gt.utils.PopupWindowMenus;

import java.util.ArrayList;
import java.util.List;

public class JumpActivityWithParamDialog extends DialogFragment {

    private static final String KEY_ACTIVITY_CLASS_NAME = "KEY_ACTIVITY_CLASS_NAME";

    private View parentView;
    private TextView classNameView;
    private CheckBox switchParamCheckBox;
    private LinearLayout paramLayout;
    private RecyclerView paramRecyclerView;
    private CommonRecyclerAdapter<ParamEntity> paramAdapter;
    private TextView addParamView;
    private Button jumpView;

    private List<ParamEntity> paramEntities = new ArrayList<>();
    private List<Class> paramTypes = new ArrayList<>();
    private String activityClassName;

    public static void showDialog(FragmentManager fragmentManager, String activityClassName) {
        JumpActivityWithParamDialog dialog = new JumpActivityWithParamDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ACTIVITY_CLASS_NAME, activityClassName);
        dialog.setArguments(bundle);
        dialog.show(fragmentManager, JumpActivityWithParamDialog.class.getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        initBundleParam();
        initParamTypes();
        parentView = inflater.inflate(R.layout.jm_gt_dialog_jump_with_param, container);
        initView(parentView);
        return parentView;
    }

    private void initBundleParam() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        if (bundle.containsKey(KEY_ACTIVITY_CLASS_NAME)) {
            activityClassName = bundle.getString(KEY_ACTIVITY_CLASS_NAME, null);
        }
    }

    private void initParamTypes() {
        paramTypes.add(String.class);
        paramTypes.add(Byte.class);
        paramTypes.add(Short.class);
        paramTypes.add(Integer.class);
        paramTypes.add(Long.class);
        paramTypes.add(Character.class);
        paramTypes.add(Float.class);
        paramTypes.add(Double.class);
        paramTypes.add(Boolean.class);
    }

    private void initView(View parentView) {
        classNameView = parentView.findViewById(R.id.tv_class_name);
        switchParamCheckBox = parentView.findViewById(R.id.cb_switch_param);
        paramLayout = parentView.findViewById(R.id.ll_param);
        paramRecyclerView = parentView.findViewById(R.id.rv_param);
        addParamView = parentView.findViewById(R.id.tv_add_param);
        jumpView = parentView.findViewById(R.id.btn_jump);

        if (TextUtils.isEmpty(activityClassName)) {
            classNameView.setVisibility(View.GONE);
        } else {
            classNameView.setVisibility(View.VISIBLE);
            classNameView.setText(activityClassName);
        }

        switchParamCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                paramLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        paramAdapter = new CommonRecyclerAdapter<ParamEntity>(getActivity(), paramRecyclerView, paramEntities, R.layout.jm_gt_item_jump_param) {
            @Override
            protected void onBind(CommonViewHolder viewHolder, View itemView, final int position, int viewType, final ParamEntity data) {
                EditText keyView = itemView.findViewById(R.id.et_key);
                EditText valueView = itemView.findViewById(R.id.et_value);
                final TextView typeView = itemView.findViewById(R.id.tv_type);
                ImageView deleteView = itemView.findViewById(R.id.iv_delete);

                setKeyEditTextListener(keyView, data);
                setValueEditTextListener(valueView, data);

                if (data.key != null) {
                    keyView.setText(data.key);
                } else {
                    keyView.setText("");
                }
                if (data.value != null) {
                    valueView.setText(data.value);
                } else {
                    valueView.setText("");
                }
                if (data.paramType == null) {
                    data.paramType = String.class;
                }
                typeView.setText(data.paramType.getSimpleName());
                typeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTypeChoosePopupWindow(typeView, position);
                    }
                });
                deleteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteParam(position);
                    }
                });
            }

        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        paramAdapter.setLayoutManager(layoutManager, 1, Color.parseColor("#DADADA"));
        paramRecyclerView.setAdapter(paramAdapter);

        addParamView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addParam();
            }
        });

        jumpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump();
            }
        });
    }

    private void setKeyEditTextListener(EditText keyView, final ParamEntity data) {
        if (keyView.getTag() != null && keyView.getTag() instanceof TextWatcher) {
            keyView.removeTextChangedListener((TextWatcher) keyView.getTag());
        }
        TextWatcher keyTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                data.key = s.toString();
            }
        };
        keyView.addTextChangedListener(keyTextWatcher);
        keyView.setTag(keyTextWatcher);
    }

    private void setValueEditTextListener(EditText valueView, final ParamEntity data) {
        if (valueView.getTag() != null && valueView.getTag() instanceof TextWatcher) {
            valueView.removeTextChangedListener((TextWatcher) valueView.getTag());
        }
        TextWatcher valueTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                data.value = s.toString();
            }
        };
        valueView.addTextChangedListener(valueTextWatcher);
        valueView.setTag(valueTextWatcher);
    }

    private void showTypeChoosePopupWindow(TextView typeView, final int position) {
        new PopupWindowMenus<Class>().showPopupWindow(getActivity(),
                new TypeInitiator(typeView),
                new PopupWindowMenus.PopupWindowCallBack() {
                    @Override
                    public void callback(int index) {
                        paramEntities.get(position).paramType = paramTypes.get(index);
                        paramAdapter.setDatas(paramEntities);
                        paramAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void deleteParam(int position) {
        paramEntities.remove(position);
        paramAdapter.setDatas(paramEntities);
        paramAdapter.notifyDataSetChanged();
    }

    private void addParam() {
        if (canAddParam()) {
            ParamEntity paramEntity = new ParamEntity();
            paramEntity.paramType = String.class;
            paramEntities.add(paramEntity);
            paramAdapter.setDatas(paramEntities);
        } else {
            Toast.makeText(getActivity(), "当前有未输入内容，请继续输入", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean canAddParam() {
        if (paramEntities.isEmpty()) {
            return true;
        }
        for (ParamEntity entity : paramEntities) {
            if (TextUtils.isEmpty(entity.key)
                    || TextUtils.isEmpty(entity.value)
                    || entity.paramType == null) {
                return false;
            }
        }
        return true;
    }

    private void jump() {
        Intent intent = createJumpIntent();
        if (intent == null) {
            Toast.makeText(getActivity(), "参数错误", Toast.LENGTH_SHORT).show();
            return;
        }
        getActivity().startActivity(intent);
        dismissAllowingStateLoss();
    }

    private Intent createJumpIntent() {
        if (TextUtils.isEmpty(activityClassName)) {
            return null;
        }
        try {
            Intent intent = new Intent(getActivity(), Class.forName(activityClassName));
            if (!switchParamCheckBox.isChecked()) {
                return intent;
            }
            for (ParamEntity paramEntity : paramEntities) {
                if (!setWhenCheckParamValid(intent, paramEntity)) {
                    return null;
                }
            }
            return intent;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean setWhenCheckParamValid(Intent intent, ParamEntity paramEntity) {
        if (paramEntity == null
                || TextUtils.isEmpty(paramEntity.key)
                || TextUtils.isEmpty(paramEntity.value)
                || paramEntity.paramType == null) {
            return false;
        }
        if (String.class.getSimpleName().equals(paramEntity.paramType.getSimpleName())) {
            intent.putExtra(paramEntity.key, paramEntity.value);
            return true;
        } else if (Byte.class.getSimpleName().equals(paramEntity.paramType.getSimpleName())) {
            try {
                byte b = Byte.parseByte(paramEntity.value);
                intent.putExtra(paramEntity.key, b);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else if (Short.class.getSimpleName().equals(paramEntity.paramType.getSimpleName())) {
            try {
                short s = Short.parseShort(paramEntity.value);
                intent.putExtra(paramEntity.key, s);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else if (Integer.class.getSimpleName().equals(paramEntity.paramType.getSimpleName())) {
            try {
                int i = Integer.parseInt(paramEntity.value);
                intent.putExtra(paramEntity.key, i);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else if (Long.class.getSimpleName().equals(paramEntity.paramType.getSimpleName())) {
            try {
                long l = Long.parseLong(paramEntity.value);
                intent.putExtra(paramEntity.key, l);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else if (Character.class.getSimpleName().equals(paramEntity.paramType.getSimpleName())) {
            if (paramEntity.value.length() == 1) {
                intent.putExtra(paramEntity.key, paramEntity.value.charAt(0));
                return true;
            } else {
                return false;
            }
        } else if (Float.class.getSimpleName().equals(paramEntity.paramType.getSimpleName())) {
            try {
                float f = Float.parseFloat(paramEntity.value);
                intent.putExtra(paramEntity.key, f);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else if (Double.class.getSimpleName().equals(paramEntity.paramType.getSimpleName())) {
            try {
                double d = Double.parseDouble(paramEntity.value);
                intent.putExtra(paramEntity.key, d);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else if (Boolean.class.getSimpleName().equals(paramEntity.paramType.getSimpleName())) {
            try {
                if ("false".equalsIgnoreCase(paramEntity.value)) {
                    intent.putExtra(paramEntity.key, false);
                    return true;
                } else if ("true".equalsIgnoreCase(paramEntity.value)) {
                    intent.putExtra(paramEntity.key, true);
                    return true;
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public class ParamEntity {

        public String key;
        public String value;
        // Byte Short Integer Long Character Float Double Boolean String 九种中的一种
        public Class paramType;

    }

    public class TypeInitiator implements PopupWindowMenus.PopupWindowInitiator<Class> {

        private View dependOnView;

        public TypeInitiator(View dependOnView) {
            this.dependOnView = dependOnView;
        }

        @Override
        public int popupLayoutId() {
            return R.layout.jm_gt_popup_listview;
        }

        @Override
        public int itemLayoutId() {
            return R.layout.jm_gt_item_simple_listview;
        }

        @Override
        public int listViewId() {
            return R.id.listView;
        }

        @Override
        public void onListViewDecorate(ListView listView) {
            ViewGroup.LayoutParams params = listView.getLayoutParams();
//            params.width = dip2px(getActivity(), 80);
            params.width = dependOnView.getWidth();
            listView.setLayoutParams(params);
        }

        @Override
        public void onBind(PopupWindowMenus.ViewHolder viewHolder, View itemView, int position, Class data) {
            TextView textView = itemView.findViewById(R.id.textView);
            textView.setText(data.getSimpleName());
        }

        @Override
        public List<Class> loadDatas() {
            return paramTypes;
        }

        @Override
        public View dependOnView() {
            return dependOnView;
        }

    }
}
