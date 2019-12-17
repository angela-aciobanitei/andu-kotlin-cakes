package com.ang.acb.baking.ui.recipedetails

import androidx.lifecycle.*
import com.ang.acb.baking.data.database.Step
import com.ang.acb.baking.data.repository.RecipeRepository
import javax.inject.Inject

class StepDetailsViewModel
@Inject constructor(private val recipesRepository: RecipeRepository): ViewModel() {

    private val _recipeId = MutableLiveData<Int>()
    private val _currentPosition = MutableLiveData<Int>()

    private val _currentStep = MediatorLiveData<Step>()
    val currentStep: MediatorLiveData<Step>
        get() = getStep()

    val steps = Transformations.switchMap(_recipeId) {
        recipesRepository.getRecipeSteps(it)
    }

    private var _stepsSize = 0

    fun init(recipeId: Int, stepPosition: Int) {
        _recipeId.value = recipeId
        _currentPosition.value = stepPosition
    }

    private fun getStep(): MediatorLiveData<Step> {
        // The list of steps might change, but also the step position,
        // so we need to listen to the changes of these two different
        // LiveData objects.
        val stepsLiveData = Transformations
            .switchMap(_recipeId) { recipesRepository.getRecipeSteps(it) }

        _currentStep.addSource(stepsLiveData) { newSteps ->
            if (newSteps != null && _currentPosition.value != null) {
                _stepsSize = newSteps.size
                _currentStep.value = newSteps[_currentPosition.value!!]
            }
        }

        val stepIndexLiveData = _currentPosition
        _currentStep.addSource(stepIndexLiveData) { newIndex: Int? ->
            if (newIndex != null && stepsLiveData.value != null) {
                _currentStep.value = stepsLiveData.value!![newIndex]
            }
        }

        return _currentStep
    }

    fun hasPrev():Boolean {
        // pos > 0
        return compareValues(_currentPosition.value, 0) > 0
    }

    fun hasNext(): Boolean {
        // pos + 1 < size
        return compareValues(_stepsSize, _currentPosition.value?.plus(1)) > 0
    }


    fun onPrev() {
        if (hasPrev()){
            _currentPosition.value = _currentPosition.value?.minus(1)
        }
    }

    fun onNext() {
        if (hasNext()){
            _currentPosition.value = _currentPosition.value?.plus(1)
        }
    }

    fun currentPos(): Int {
        return _currentPosition.value!!
    }
}