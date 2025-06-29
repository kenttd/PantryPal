package com.kenttravis.pantrypal.ui.chatbot

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kenttravis.pantrypal.PantryPalViewModelFactory
import com.kenttravis.pantrypal.databinding.DialogNewChatBinding
import com.kenttravis.pantrypal.databinding.FragmentChatListBinding
import com.kenttravis.pantrypal.databinding.ItemChatSessionBinding
import com.kenttravis.pantrypal.sources.local.AuthManager
import com.kenttravis.pantrypal.sources.remote.ChatHistory
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ChatListFragment : Fragment() {
    lateinit var binding: FragmentChatListBinding
    val vm by activityViewModels<ChatbotViewModel>{ PantryPalViewModelFactory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm.getHistory(AuthManager.getToken(requireContext())!!)
        binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv: RecyclerView = binding.chatListRecyclerView
        val adapter = ChatListAdapter(){data->
            val action = ChatListFragmentDirections.actionChatListFragmentToChatDetailFragment(data.id.toString())
            findNavController().navigate(action)
        }

        rv.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        rv.adapter = adapter
        vm.data.observe(requireActivity()){data->
            adapter.submitList(data)
        }

        binding.fabNewChat.setOnClickListener {
            showNewChatDialog()
        }
    }

    private fun showNewChatDialog() {
        val dialogBinding = DialogNewChatBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnSend.setOnClickListener {
            val message = dialogBinding.messageEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                // Handle sending the message here
                handleNewChatMessage(message)
                dialog.dismiss()
            } else {
                // Show error or keep dialog open
                dialogBinding.messageInputLayout.error = "Please enter a message"
            }
        }

        dialog.show()
    }

    private fun handleNewChatMessage(message: String) {
        // TODO: Implement your logic to start a new chat with the message
        // This might involve:
        // 1. Creating a new chat session
        // 2. Sending the first message
        // 3. Navigating to the chat detail screen

        // Example implementation:
        // vm.startNewChat(message) { newChatId ->
        //     val action = ChatListFragmentDirections.actionChatListFragmentToChatDetailFragment(newChatId.toString())
        //     findNavController().navigate(action)
        // }
        vm.sendMessageNew(AuthManager.getToken(requireContext())!!,message){session_id->
            val action = ChatListFragmentDirections.actionChatListFragmentToChatDetailFragment(session_id)
            findNavController().navigate(action)
        }
    }
}

class ChatListDiffUtil: DiffUtil.ItemCallback<ChatHistory>(){
    override fun areItemsTheSame(oldItem: ChatHistory, newItem: ChatHistory): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ChatHistory, newItem: ChatHistory): Boolean {
        return oldItem == newItem
    }
}

val chatListDiffUtil = ChatListDiffUtil()

class ChatListAdapter(
    private val onClick: (ChatHistory)->Unit
): ListAdapter<ChatHistory, ChatListAdapter.ViewHolder>(
    chatListDiffUtil
) {
    class ViewHolder(val binding: ItemChatSessionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemChatSessionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.binding.chatTitle.text = data.title
        holder.binding.timestamp.text = formatChatTimestamp(data.updated_at)
        holder.binding.lastMessage.text = data.last_message

        holder.binding.root.setOnClickListener{
            onClick(data)
        }
    }
}


fun formatChatTimestamp(isoString: String): String {
    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    isoFormat.timeZone = TimeZone.getTimeZone("UTC")

    val date = isoFormat.parse(isoString)

    val outputFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
    return outputFormat.format(date!!)
}