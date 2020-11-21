package com.app.pixstory.ui.messages.messageUser;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseFragment;
import com.app.pixstory.data.model.db.messages.MessageUsers;
import com.app.pixstory.databinding.FragmentMessageUserBinding;
import com.app.pixstory.ui.messages.MessagesViewModel;
import com.app.pixstory.ui.messages.message.MessageActivity;
import com.app.pixstory.ui.messages.messageUser.messageUserAdapter.MessageUserAdapter;
import com.app.pixstory.ui.messages.messageUser.messageUserAdapter.RecyclerItemTouchHelper;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.ConstantsData;
import com.app.pixstory.utils.util.Bundles;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Arvindo Mondal
 * Created on 21-02-2020
 */
public class MessageUserFragment extends BaseFragment<FragmentMessageUserBinding, MessagesViewModel> {

    private FragmentMessageUserBinding binding;
    private MessagesViewModel viewModel;
    private MessageUserAdapter adapter ;
    private String type = Constants.MESSAGE_TYPE_INBOX;
    private String bulletinType = Constants.MESSAGE_TYPE_BULLETIN_INTEREST;

    @Override
    protected Class<MessagesViewModel> setViewModel() {
        return MessagesViewModel.class;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_message_user;
    }

    @Override
    protected void getBinding(FragmentMessageUserBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(MessagesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected void onCreateFragment(Bundle savedInstanceState, Bundle args) {
        type = Bundles.getInstance().getMessageType(getArguments());
        adapter = new MessageUserAdapter(new ArrayList<>());
        subscribeToLiveData();
    }

    @Override
    protected void init() {
        binding.setData(viewModel);
        setup();
        setupMessageUserAdapter();
//        viewModel.getMessageUsersDb();
    }

    private void subscribeToLiveData() {
        viewModel.getMessageUsersLiveData().observe(this, data -> {
            viewModel.addMessageUsers(data);
        });
    }


    private void setup() {
        type = Bundles.getInstance().getMessageType(getArguments());
    }

    private void setupMessageUserAdapter() {
        adapter.setListener(this::onAdapterItem);
        adapter.setup(type);
        binding.listView.setItemAnimator(new DefaultItemAnimator());
        binding.listView.addItemDecoration(new DividerItemDecoration(getBaseActivity(), DividerItemDecoration.VERTICAL));
        binding.listView.setAdapter(adapter);
//        adapter.addItems(ConstantsData.getMessageUserList());  //call to set data

//        ItemTouchHelper itemTouchHelper = new
//                ItemTouchHelper(simpleItemTouchCallback);
//        itemTouchHelper.attachToRecyclerView(binding.listView);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0,
                ItemTouchHelper.LEFT, this::onSwiped);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.listView);

        if(type.equals(Constants.MESSAGE_TYPE_INBOX)){
            viewModel.getMessageUsersApis();
        }
        else{
            viewModel.getMessageUsersBulletinApis(bulletinType);
        }
    }

    private void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position){
        if (viewHolder != null) {
            // get the removed item name to display it in snack bar
            String name = adapter.getData(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final MessageUsers deletedItem = adapter.getData(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            viewModel.removeCard(deletedItem.getUserId());
            adapter.removeItem(viewHolder.getAdapterPosition());

            //TODO open this to undo delete last item
/*
            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(binding.layout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", view -> {

                // undo is selected, restore the deleted item
                adapter.restoreItem(deletedItem, deletedIndex);
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
*/
        }
    }

    private void onAdapterItem(Object model, String tag, int position){
        if (model instanceof MessageUsers){
            if(type.equals(Constants.MESSAGE_TYPE_INBOX)){
                getBaseActivity().startActivity(MessageActivity.class, Bundles.getInstance().setMessage(type,
                        ((MessageUsers) model).getUserId(), ((MessageUsers) model).getUserName()));
            }else if(type.equals(Constants.MESSAGE_TYPE_BULLETIN)){
                getBaseActivity().startActivity(MessageActivity.class, Bundles.getInstance().setMessage(type,
                        ((MessageUsers) model).getMessage().getBulletinId(), ((MessageUsers) model).getUserName()));
            }
        }
    }

    //When new message send to new user from activity
    public void onNewMessageSend(List<MessageUsers> messageUsers) {
        viewModel.onNewMessageSend(type, messageUsers, bulletinType);
    }

    public void onBulletinType(String bulletinType) {
        this.bulletinType = bulletinType;
        viewModel.getMessageUsersBulletinApis(bulletinType);
    }
}