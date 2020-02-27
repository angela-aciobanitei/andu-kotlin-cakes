package com.ang.acb.baking.ui.recipedetails

import androidx.lifecycle.*
import androidx.lifecycle.Transformations.*
import com.ang.acb.baking.data.database.Step
import com.ang.acb.baking.data.network.Resource
import com.ang.acb.baking.data.repository.RecipeRepository
import javax.inject.Inject

/**
 * The [ViewModel] for [StepDetailsFragment].
 */
class StepDetailsViewModel @Inject constructor(
    private val recipesRepository: RecipeRepository
): ViewModel() {

    private val _recipeId = MutableLiveData<Int>()

    private val _currentPosition = MutableLiveData<Int>()
    private val currentPosition: LiveData<Int>
        get() = _currentPosition

    private var _stepsSize = 0

    private val steps = switchMap(_recipeId, recipesRepository::getRecipeSteps)

    val step = currentStep()

    fun init(recipeId: Int, stepPosition: Int) {
        _recipeId.value = recipeId
        _currentPosition.value = stepPosition
    }

    // See: https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7
    private fun currentStep(): MediatorLiveData<Resource<Step>> {

        val positionResult = currentPosition
        val stepsResult = steps

        val result = MediatorLiveData<Resource<Step>>()

        result.addSource(positionResult) {
            result.value = combineLatestData(positionResult, stepsResult)
        }
        result.addSource(stepsResult) {
            result.value = combineLatestData(positionResult, stepsResult)
        }
        return result
    }

    private fun combineLatestData(
        positionResult: LiveData<Int>,
        stepsResult: LiveData<List<Step>>
    ): Resource<Step> {

        val position = positionResult.value
        val steps = stepsResult.value

        return if(position != null && steps != null) {
            _stepsSize = steps.size
            Resource.success(steps[position])
        }else if(position == null || steps == null) {
            Resource.loading(null)
        } else {
            Resource.error("Error", null)
        }
    }

    fun hasPrev(): Boolean {
        // pos > 0
        return compareValues(_currentPosition.value, 0) > 0
    }

    fun hasNext(): Boolean {
        // pos + 1 < size
        return compareValues(_stepsSize, _currentPosition.value?.plus(1)) > 0
    }

    fun onPrev() {
        if (hasPrev()) _currentPosition.value = _currentPosition.value?.minus(1)
    }

    fun onNext() {
        if (hasNext()) _currentPosition.value = _currentPosition.value?.plus(1)
    }
}