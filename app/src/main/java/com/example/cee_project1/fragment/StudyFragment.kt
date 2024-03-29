package com.example.cee_project1.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cee_project1.CEEApplication
import com.example.cee_project1.CEEApplication.Companion.prefs
import com.example.cee_project1.activity.TermActivity
import com.example.cee_project1.activity.TermListActivity
import com.example.cee_project1.data.Term
import com.example.cee_project1.databinding.FragmentStudyBinding
import com.example.cee_project1.dialog.TutorialDialogFragment
import io.realm.Realm
import io.realm.kotlin.where

class StudyFragment : Fragment() {

    lateinit var binding : FragmentStudyBinding
    var tutorialCnt:Int=0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyBinding.inflate(inflater, container, false)

        binding.studySeeListBtn.setOnClickListener {
            val intent = Intent(context, TermListActivity::class.java)
            startActivity(intent)
        }

        binding.studyContinueBtn.setOnClickListener {
            val intent = Intent(context, TermActivity::class.java)
            val id = prefs.getString("term_save_point", "-1")
            intent.putExtra("id", id.toInt())
            startActivity(intent)
        }

        tutorialCnt= prefs.getString("tutorial_cnt","0").toInt()
        Log.d("tutorialCnt",tutorialCnt.toString())
        if(tutorialCnt==0){
            showTutorial()
        }
        return binding.root
    }

    override fun onResume() {
        val pos = prefs.getString("term_save_point", "0").toInt()
        val text = Realm.getDefaultInstance().where<Term>().containsValue("id", pos).findFirst()?.name
        binding.studyPositionTv.text = text + "입니다."
        super.onResume()
    }

    private fun showTutorial() {
        val TutoDialog = TutorialDialogFragment()
        TutoDialog.show(requireFragmentManager() ,"TutorialDialogFragment")
        tutorialCnt++
        prefs.setString("tutorial_cnt",tutorialCnt.toString())
    }


}