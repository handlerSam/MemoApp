package com.example.memoryapplication;


import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.ViewHolder> implements Filterable {
    private List<Password> mpassword;
    private List<Password> mFilterList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        LinearLayout userLayout;
        LinearLayout passwordLayout;
        LinearLayout password2Layout;
        LinearLayout remarkLayout;
        TextView userText;
        TextView passwordText;
        TextView password2Text;
        TextView remarkText;
        TextView idText;
        public ViewHolder(View view){
            super(view);
            title = (TextView)view.findViewById(R.id.title);
            userLayout = (LinearLayout) view.findViewById(R.id.user);
            passwordLayout = (LinearLayout) view.findViewById(R.id.password1);
            password2Layout = (LinearLayout) view.findViewById(R.id.password2);
            remarkLayout = (LinearLayout) view.findViewById(R.id.remark);
            userText = (TextView)view.findViewById(R.id.userText);
            passwordText = (TextView)view.findViewById(R.id.password1Text);
            password2Text = (TextView)view.findViewById(R.id.password2Text);
            remarkText = (TextView)view.findViewById(R.id.remarkText);
            idText = (TextView)view.findViewById(R.id.passwordId);
        }
    }

    public PasswordAdapter(List<Password> passwordList){
        mpassword = passwordList;
        mFilterList = passwordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.password_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        final ViewHolder tempHolder = holder;
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Main2Activity.main2Activity);
                dialog.setTitle("选择");//标题
                dialog.setPositiveButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Main2Activity.main2Activity,addActivity.class);
                        intent.putExtra("id",tempHolder.idText.getText().toString());
                        intent.putExtra("t",tempHolder.title.getText().toString());
                        intent.putExtra("u",tempHolder.userText.getText().toString());
                        intent.putExtra("p1",tempHolder.passwordText.getText().toString());
                        intent.putExtra("p2",tempHolder.password2Text.getText().toString());
                        intent.putExtra("r",tempHolder.remarkText.getText().toString());
                        Main2Activity.main2Activity.startActivityForResult(intent, 1);
                    }});
                dialog.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder dialog2 = new AlertDialog.Builder(Main2Activity.main2Activity);
                        dialog2.setTitle("确认删除？");//标题
                        dialog2.setMessage("该操作无法回退");//正文
                        dialog2.setCancelable(true);//是否能点击屏幕取消该弹窗
                        dialog2.setPositiveButton("还是算了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}});
                        dialog2.setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //错误逻辑
                                String id = tempHolder.idText.getText().toString();
                                SharedPreferences.Editor editor = Main2Activity.main2Activity.getSharedPreferences("password", MODE_PRIVATE).edit();
                                editor.putString("p"+id+"t","");
                                editor.putString("p"+id+"u","");
                                editor.putString("p"+id+"p1","");
                                editor.putString("p"+id+"p2","");
                                editor.putString("p"+id+"r","");
                                editor.apply();
                                Main2Activity.main2Activity.loadList();
                                PasswordAdapter.this.notifyDataSetChanged();
                            }});
                        dialog2.show();
                    }});
                dialog.show();
                return true;
            }
        });
        holder.userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager)Main2Activity.main2Activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label",tempHolder.userText.getText().toString());
                cm.setPrimaryClip(mClipData);
                Toast.makeText(Main2Activity.main2Activity,"已复制用户名",Toast.LENGTH_SHORT).show();
            }
        });
        holder.passwordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager)Main2Activity.main2Activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label",tempHolder.passwordText.getText().toString());
                cm.setPrimaryClip(mClipData);
                Toast.makeText(Main2Activity.main2Activity,"已复制密码",Toast.LENGTH_SHORT).show();
            }
        });
        holder.password2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager)Main2Activity.main2Activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label",tempHolder.password2Text.getText().toString());
                cm.setPrimaryClip(mClipData);
                Toast.makeText(Main2Activity.main2Activity,"已复制密码2",Toast.LENGTH_SHORT).show();
            }
        });
        holder.remarkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager)Main2Activity.main2Activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label",tempHolder.remarkText.getText().toString());
                cm.setPrimaryClip(mClipData);
                Toast.makeText(Main2Activity.main2Activity,"已复制备注",Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Password password = mFilterList.get(position);
        holder.title.setText(password.title);
        holder.idText.setText(String.valueOf(password.id));
        if(!password.password.equals("")){
            holder.passwordLayout.setVisibility(View.VISIBLE);
            holder.passwordText.setText(password.password);
        }else{
            holder.passwordText.setText("");
            holder.passwordLayout.setVisibility(View.GONE);
        }
        if(!password.password2.equals("")){
            holder.password2Layout.setVisibility(View.VISIBLE);
            holder.password2Text.setText(password.password2);
        }else{
            holder.password2Text.setText("");
            holder.password2Layout.setVisibility(View.GONE);
        }
        if(!password.remark.equals("")){
            holder.remarkLayout.setVisibility(View.VISIBLE);
            holder.remarkText.setText(password.remark);
        }else{
            holder.remarkText.setText("");
            holder.remarkLayout.setVisibility(View.GONE);
        }
        if(!password.user.equals("")){
            holder.userLayout.setVisibility(View.VISIBLE);
            holder.userText.setText(password.user);
        }else{
            holder.userText.setText("");
            holder.userLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    mFilterList = mpassword;
                } else {
                    List<Password> filteredList = new ArrayList<>();
                    for (Password pas : mpassword) {
                        //这里根据需求，添加匹配规则
                        if (pas.title.contains(charString)) {
                            filteredList.add(pas);
                        }
                    }

                    mFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterList;
                return filterResults;
            }
            //把过滤后的值返回出来
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilterList = (ArrayList<Password>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

}

class Password{
    String title = "";
    String user = "";
    String password = "";
    String password2 = "";
    String remark = "";
    int id;
}
