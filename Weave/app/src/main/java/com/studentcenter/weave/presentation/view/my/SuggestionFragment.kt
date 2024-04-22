package com.studentcenter.weave.presentation.view.my

import android.text.Editable
import android.text.TextWatcher
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.data.remote.dto.weave.SubmitSuggestionReq
import com.studentcenter.weave.databinding.FragmentSuggestionBinding
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.weave.SubmitSuggestionUseCase
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.custom.CustomToast
import com.studentcenter.weave.presentation.util.CustomDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SuggestionFragment: BaseFragment<FragmentSuggestionBinding>(R.layout.fragment_suggestion) {
    override fun init() {
        binding.btnNext.alpha = if(binding.etSuggestionContent.text.isNotEmpty()) 1f else 0.6f
        binding.btnNext.isClickable = binding.etSuggestionContent.text.isNotEmpty()

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnNext.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

                when(val res = SubmitSuggestionUseCase().submitSuggestion(accessToken, SubmitSuggestionReq(binding.etSuggestionContent.text.toString()))){
                    is Resource.Success -> {
                        launch(Dispatchers.Main){
                            CustomDialog.getInstance(CustomDialog.DialogType.SUGGESTION, null).apply {
                                setOnOKClickedListener {
                                        this@SuggestionFragment.requireActivity().supportFragmentManager.popBackStack()
                                }
                            }.show(this@SuggestionFragment.requireActivity().supportFragmentManager, "")
                        }
                    }
                    is Resource.Error -> {
                        launch(Dispatchers.Main){
                            CustomToast.createToast(this@SuggestionFragment.requireContext(), res.message).show()
                        }
                    }
                    else -> {}
                }
            }
        }

        binding.etSuggestionContent.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                binding.btnNext.alpha = if(binding.etSuggestionContent.text.isNotEmpty()) 1f else 0.6f
                binding.btnNext.isClickable = binding.etSuggestionContent.text.isNotEmpty()
            }
        })
    }
}