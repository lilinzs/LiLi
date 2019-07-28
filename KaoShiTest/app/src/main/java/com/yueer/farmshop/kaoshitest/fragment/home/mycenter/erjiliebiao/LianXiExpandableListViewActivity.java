package com.yueer.farmshop.kaoshitest.fragment.home.mycenter.erjiliebiao;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;


import com.yueer.farmshop.kaoshitest.R;

import java.util.ArrayList;


public class LianXiExpandableListViewActivity extends Activity {


    //View
    private ExpandableListView expandableListView;

//    //Model：定义的数据
//    private String[] groups = {"Ahdjsojpodjop", "B", "C"};
//
//    //注意，字符数组不要写成{{"A1,A2,A3,A4"}, {"B1,B2,B3,B4，B5"}, {"C1,C2,C3,C4"}}
//    private String[][] childs = {{"A1", "A2", "A3", "A4"}, {"A1", "A2", "A3", "B4"}, {"A1", "A2", "A3", "C4"}};

//    //一级列表数据源
//    private String[] groups = {"软件设计", "数据库技术", "操作系统"};
//    //二级列表数据源
//    private String[][] childs = {{"架构设计", "面向对象", "设计模式", "领域驱动设计"}, {"SQL Server", "Oracle", "MySql", "Dameng "}, {"Linux", "Windows", "嵌入式"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lian_xi_expandable_list_view);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        initData();

        MyAdapter adapter = new MyAdapter(this, mGroupList, mItemSet);
        expandableListView.setAdapter(adapter);
//        expandableListView.setAdapter(new MyExpandableListView());
//        initListenner();
    }


    class MyAdapter extends BaseExpandableListAdapter {

        private Context mContext;
        private ArrayList<String> mGroup;
        private ArrayList<ArrayList<String>> mItemList;
        private final LayoutInflater mInflater;


        public MyAdapter(Context context, ArrayList<String> group, ArrayList<ArrayList<String>> itemList){
            this.mContext = context;
            this.mGroup = group;
            this.mItemList = itemList;
            mInflater = LayoutInflater.from(context);
        }

        //父项的个数
        @Override
        public int getGroupCount() {
            return mGroup.size();
        }

        //某个父项的子项的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            return mItemList.get(groupPosition).size();
        }

        //获得某个父项
        @Override
        public Object getGroup(int groupPosition) {
            return mGroup.get(groupPosition);
        }
        //获得某个子项
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mItemList.get(groupPosition).get(childPosition);
        }

        //父项的Id
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        //子项的id

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        //获取父项的view
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = mInflater.inflate(R.layout.item_group,parent,false);
            }
            String group = mGroup.get(groupPosition);
            TextView tvGroup = (TextView) convertView.findViewById(R.id.tv_group);
            tvGroup.setText(group);
            return convertView;
        }

        //获取子项的view
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final String child = mItemList.get(groupPosition).get(childPosition);
            if (convertView == null){
                convertView = mInflater.inflate(R.layout.item_child,parent,false);
            }

//            TextView tvChild = (TextView)convertView.findViewById(R.id.tv_name);
//            tvChild.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(mContext,child,Toast.LENGTH_SHORT).show();
//                }
//            });
//            tvChild.setText(child);
            return convertView;

        }

        //子项是否可选中,如果要设置子项的点击事件,需要返回true
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
//     监听
//    private void initListenner() {
//        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                TextView childAt = (TextView)((LinearLayout) v).getChildAt(1);//获得点击列表中TextView的值，需要强转一下，否则找不到getChildAt方法
//                Toast.makeText(LianXiExpandableListViewActivity.this, "点击了 "+childAt.getText()+" 列表", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
//    }


//    //为ExpandableListView自定义适配器
//    class MyExpandableListView extends BaseExpandableListAdapter {
//
//        //返回一级列表的个数
//        @Override
//        public int getGroupCount() {
//            return groups.length;
//        }
//
//        //返回每个二级列表的个数
//        @Override
//        public int getChildrenCount(int groupPosition) { //参数groupPosition表示第几个一级列表
//            Log.d("smyhvae", "-->" + groupPosition);
//            return childs[groupPosition].length;
//        }
//
//        //返回一级列表的单个item（返回的是对象）
//        @Override
//        public Object getGroup(int groupPosition) {
//            return groups[groupPosition];
//        }
//
//        //返回二级列表中的单个item（返回的是对象）
//        @Override
//        public Object getChild(int groupPosition, int childPosition) {
//            return childs[groupPosition][childPosition];  //不要误写成groups[groupPosition][childPosition]
//        }
//
//        @Override
//        public long getGroupId(int groupPosition) {
//            return groupPosition;
//        }
//
//        @Override
//        public long getChildId(int groupPosition, int childPosition) {
//            return childPosition;
//        }
//
//        //每个item的id是否是固定？一般为true
//        @Override
//        public boolean hasStableIds() {
//            return true;
//        }
//
//        //【重要】填充一级列表
//        @Override
//        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//
//            if (convertView == null) {
//                convertView = getLayoutInflater().inflate(R.layout.item_group, null);
//            } else {
//
//            }
//            TextView tv_group = (TextView) convertView.findViewById(R.id.tv_group);
//            tv_group.setText(groups[groupPosition]);
//            return convertView;
//        }
//
//        //【重要】填充二级列表
//        @Override
//        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//
//            if (convertView == null) {
//                convertView = getLayoutInflater().inflate(R.layout.item_child, null);
//            }
//
//            ImageView iv_child = (ImageView) convertView.findViewById(R.id.iv_child);
//            TextView tv_child = (TextView) convertView.findViewById(R.id.tv_child);
//
//            //iv_child.setImageResource(resId);
//            tv_child.setText(childs[groupPosition][childPosition]);
//
//            return convertView;
//        }
//
//        //二级列表中的item是否能够被选中？可以改为true    //子项是否可选中,如果要设置子项的点击事件,需要返回true
//        @Override
//        public boolean isChildSelectable(int groupPosition, int childPosition) {
//            return true;
//        }
//    }

    //group数据
    private ArrayList<String> mGroupList;
    //item数据
    private ArrayList<ArrayList<String>> mItemSet;
    private void initData() {
        mGroupList = new ArrayList<>();
        mGroupList.add("我的家人");
        mGroupList.add("我的朋友");
        mGroupList.add("黑名单");

        mItemSet = new ArrayList<>();

        ArrayList<String> itemList1 = new ArrayList<>();
        itemList1.add("大妹");
        itemList1.add("二妹");
        itemList1.add("三妹");
        ArrayList<String> itemList2 = new ArrayList<>();
        itemList2.add("大美");
        itemList2.add("二美");
        itemList2.add("三美");
        ArrayList<String> itemList3 = new ArrayList<>();
        itemList3.add("狗蛋");
        itemList3.add("二丫");
        mItemSet.add(itemList1);
        mItemSet.add(itemList2);
        mItemSet.add(itemList3);
    }
}
/*


步骤:
        ① 布局中定义ExpandableListView控件
        ② 初始化Group数据,Child数据(ArrayList<ArrayList<Info>>)
        ③ 适配器BaseExpandaListAdapter
        重写10个方法 getGroupCount(),getGroup(),getGroupId(),getGroupView()
        getChildCount(),getChild(),getChildId(),getChildView()
        hasStablelds()行是否具有唯一id  isChildSelectable() 子列表是否可点击事件

        作者：君袅
        链接：https://www.jianshu.com/p/b995c71e36f0
        来源：简书
        简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。

属性:
        divider       父类分割线
        childDivider  子类分割线
        dividerHeight  分割线高度
        groupIndicator  控制父类前面的小箭头
        android:groupIndicator="@drawable/picture"  箭头换照片
        android:groupIndicator="@null"  去掉小箭头
        indicatorLeft  箭头或者自己设置的图片的右边框距离手机左边边缘的距离,类似于marginLeft
        indicatorStart  箭头或者自己设置的图片的左边框距离手机左边边缘的距离,类似于marginLeft以上两个属性相结合可以使箭头或自己设置的图片变大变小或者翻转,因为控制了箭头或图片的左右边框
        childIndicator  用于设置子项前显示的图标,不设置的话默认是没有图标的
//设置图标选择
<item android:drawable=”@drawable/icon_t” android:state_expanded=”true”/>
<item android:drawable=”@drawable/icon_f” android:state_expanded=”false”/>

点击事件:
        对于处理 Item 的点击事件，还是要设置监听器，常用的有这几类：
        setOnChildClickListener//单击子选项
        setOnGroupClickListener//单击组选项
        setOnGroupCollapseListener//分组合并
        setOnGroupExpandListener//分组合并

*/
