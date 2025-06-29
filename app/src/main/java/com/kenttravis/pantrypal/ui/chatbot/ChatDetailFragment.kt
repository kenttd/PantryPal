package com.kenttravis.pantrypal.ui.chatbot

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kenttravis.pantrypal.PantryPalViewModelFactory
import com.kenttravis.pantrypal.databinding.FragmentChatDetailBinding
import com.kenttravis.pantrypal.databinding.ItemMessageModelBinding
import com.kenttravis.pantrypal.databinding.ItemMessageUserBinding
import com.kenttravis.pantrypal.sources.local.AuthManager
import com.kenttravis.pantrypal.sources.remote.ChatDetail
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ChatDetailFragment : Fragment() {
    private lateinit var binding: FragmentChatDetailBinding
    private val vm by activityViewModels<ChatbotViewModel> { PantryPalViewModelFactory }
    val args: ChatDetailFragmentArgs by navArgs()
    private var chatTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        vm.getDetailChat(AuthManager.getToken(requireContext())!!, args.id)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        setupInputHandling()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.toolbarTitle.text = chatTitle ?: "Chat"
        binding.toolbarSubtitle.text = "Active now" // You can update this based on your logic

        binding.menuButton.setOnClickListener {
            // Handle menu button click - show options menu
            showChatOptions()
        }
    }

    private fun setupRecyclerView() {
        val adapter = ChatMessageAdapter()
        val layoutManager = LinearLayoutManager(requireContext())

        binding.messagesRecyclerView.layoutManager = layoutManager
        binding.messagesRecyclerView.adapter = adapter

        // Observe messages
        vm.detail.observe(viewLifecycleOwner) { messages ->
            adapter.submitList(messages.messages) {
                // Scroll to bottom when new message is added
                if (messages.messages.isNotEmpty()) {
                    binding.messagesRecyclerView.scrollToPosition(messages.messages.size - 1)
                }
                showTypingIndicator(false)
            }
        }
    }

    private fun setupInputHandling() {
        binding.sendButton.setOnClickListener {
            val messageText = binding.messageInput.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
            }
        }

        // Handle text input changes
        binding.messageInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // Scroll to bottom when input is focused
                scrollToBottom()
            }
        }
    }

    private fun sendMessage(message: String) {

        binding.messageInput.text?.clear()

        // Show typing indicator
        showTypingIndicator(true)
        vm.sendMessage(AuthManager.getToken(requireContext())!!, message, args.id){
            vm.getDetailChat(AuthManager.getToken(requireContext())!!,args.id)
        }
    }

    private fun showTypingIndicator(show: Boolean) {
        binding.typingIndicator.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun scrollToBottom() {
        val adapter = binding.messagesRecyclerView.adapter
        adapter?.let {
            if (it.itemCount > 0) {
                binding.messagesRecyclerView.smoothScrollToPosition(it.itemCount - 1)
            }
        }
    }

    private fun showChatOptions() {
        // Implement chat options menu (delete chat, export, etc.)
        Toast.makeText(requireContext(), "Chat options", Toast.LENGTH_SHORT).show()
    }


//    companion object {
//        fun newInstance(sessionId: String, chatTitle: String): ChatDetailFragment {
//            return ChatDetailFragment().apply {
//                arguments = Bundle().apply {
//                    putString("session_id", sessionId)
//                    putString("chat_title", chatTitle)
//                }
//            }
//        }
//    }
}

class ChatMessageDiffUtil : DiffUtil.ItemCallback<ChatDetail>() {
    override fun areItemsTheSame(oldItem: ChatDetail, newItem: ChatDetail): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ChatDetail, newItem: ChatDetail): Boolean {
        return oldItem == newItem
    }
}

val chatMessageDiffUtil = ChatMessageDiffUtil()

class ChatMessageAdapter : ListAdapter<ChatDetail, RecyclerView.ViewHolder>(
    chatMessageDiffUtil
) {
    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_MODEL = 2
    }

    class UserMessageViewHolder(val binding: ItemMessageUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ModelMessageViewHolder(val binding: ItemMessageModelBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).sender) {
            "user" -> VIEW_TYPE_USER
            "model" -> VIEW_TYPE_MODEL
            else -> VIEW_TYPE_USER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val binding = ItemMessageUserBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                UserMessageViewHolder(binding)
            }
            VIEW_TYPE_MODEL -> {
                val binding = ItemMessageModelBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                ModelMessageViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)

        when (holder) {
            is UserMessageViewHolder -> {
                holder.binding.messageText.text = message.message
                holder.binding.messageTimestamp.text = formatMessageTimestamp(message.timestamp)
            }
            is ModelMessageViewHolder -> {
                holder.binding.messageText.text = message.message
                holder.binding.messageTimestamp.text = formatMessageTimestamp(message.timestamp)
            }
        }
    }
}

fun formatMessageTimestamp(isoString: String): String {
    return try {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date = isoFormat.parse(isoString)

        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        outputFormat.format(date!!)
    } catch (e: Exception) {
        // Fallback for parsing errors
        "Now"
    }
}