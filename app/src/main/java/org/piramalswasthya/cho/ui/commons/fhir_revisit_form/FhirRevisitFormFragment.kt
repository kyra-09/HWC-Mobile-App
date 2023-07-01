package org.piramalswasthya.cho.ui.commons.fhir_revisit_form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.android.fhir.datacapture.QuestionnaireFragment
import org.hl7.fhir.r4.model.QuestionnaireResponse
import org.piramalswasthya.cho.R
import org.piramalswasthya.cho.ui.commons.fhir_patient_vitals.FhirVitalsFragment
import org.piramalswasthya.cho.ui.commons.fhir_patient_vitals.FhirVitalsViewModel
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [FhirRevisitFormFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FhirRevisitFormFragment : Fragment(R.layout.fragment_revisit_form) {

    private val viewModel: FhirRevisitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("initiated")
        super.onViewCreated(view, savedInstanceState)
        setUpActionBar()
        setHasOptionsMenu(true)
        updateArguments()
        if (savedInstanceState == null) {
            addQuestionnaireFragment()
        }
        observePatientSaveAction()
    }

    private fun setUpActionBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Revisit and Refer"
            setDisplayHomeAsUpEnabled(true)
        }
    }


    private fun updateArguments() {
        arguments = Bundle()
        requireArguments().putString(QUESTIONNAIRE_FILE_PATH_KEY, "revisit_form.json")
    }

    private fun observePatientSaveAction() {
        viewModel.isPatientSaved.observe(viewLifecycleOwner) {
            if (!it) {
                Toast.makeText(requireContext(), "Inputs are missing.", Toast.LENGTH_SHORT).show()
                return@observe
            }
            Toast.makeText(requireContext(), "Patient is saved.", Toast.LENGTH_SHORT).show()
//            NavHostFragment.findNavController(this).navigateUp()
        }
    }

    private fun addQuestionnaireFragment() {
        childFragmentManager.commit {
            add(
                R.id.revisit_from_container,
                QuestionnaireFragment.builder().setQuestionnaire(viewModel.questionnaire).build(),
                QUESTIONNAIRE_FRAGMENT_TAG
            )
        }
    }


    private fun onSubmitAction() {
        val questionnaireFragment =
            childFragmentManager.findFragmentByTag(FhirRevisitFormFragment.QUESTIONNAIRE_FRAGMENT_TAG) as QuestionnaireFragment
        savePatient(questionnaireFragment.getQuestionnaireResponse())
    }

    private fun savePatient(questionnaireResponse: QuestionnaireResponse) {
        viewModel.savePatient(questionnaireResponse)
    }





    companion object {
        const val QUESTIONNAIRE_FILE_PATH_KEY = "questionnaire-file-path-key"
        const val QUESTIONNAIRE_FRAGMENT_TAG = "questionnaire-fragment-tag"
    }
}