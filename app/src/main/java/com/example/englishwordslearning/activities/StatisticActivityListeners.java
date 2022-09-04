package com.example.englishwordslearning.activities;

import static com.example.englishwordslearning.Constants.Toasts.HOW_TO_RESET_PROGRESS;
import static com.example.englishwordslearning.Constants.Toasts.MODE_NOT_AVAILABLE;
import static com.example.englishwordslearning.Constants.Toasts.PROGRESS_RESETED;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import com.example.englishwordslearning.Dictionary;
import com.example.englishwordslearning.ToastUtil;
import com.example.englishwordslearning.modes.LearningNewMode;
import com.example.englishwordslearning.modes.RepetitionMode;
import com.example.englishwordslearning.modes.StandartMode;

/**
 * Предоставляет обработчики событий для элементов InfoActivity
 */
public class StatisticActivityListeners {

    private final StatisticActivity activity;

    public StatisticActivityListeners(StatisticActivity activity) {
        this.activity = activity;
    }

    /**
     * @return Предоставляет обработчик клика кнопки возврата на основной экран
     */
    View.OnClickListener getBackClickListener() {
        return x -> {
            Intent intent = new Intent(this.activity, MainActivity.class);
            this.activity.startActivityForResult(intent, 1);
        };
    }

    /**
     * @return Предоставляет обработчик долгого нажатия кнопки сброса статистики
     */
    @SuppressLint("ClickableViewAccessibility")
    View.OnTouchListener getResetTouchListener() {
        return (btn, event) -> {
            if (btn.isPressed() && event.getAction() == MotionEvent.ACTION_UP) {
                long eventDuration = event.getEventTime() - event.getDownTime();
                if (eventDuration > 3000) {
                    Dictionary.resetProgress(this.activity);
                    this.activity.recreate();
                    ToastUtil.showToast(PROGRESS_RESETED, this.activity);
                } else {
                    ToastUtil.showToast(HOW_TO_RESET_PROGRESS, this.activity);
                }
            }
            return false;
        };
    }

    /**
     * @return Предоставляет обработчик клика кнопки перехода в стандартный режим
     */
    View.OnClickListener getStandartModeListener() {
        return x -> {
            Dictionary.changeMode(new StandartMode());
            this.activity.fillStatistic();
        };
    }

    /**
     * @return Предоставляет обработчик клика кнопки перехода в режим изучения нового
     */
    View.OnClickListener getLearningNewModeClickListener() {
        return x -> {
            if (Dictionary.first300WordsDistributed()) {
                Dictionary.changeMode(new LearningNewMode());
                this.activity.fillStatistic();
            } else {
                ToastUtil.showToast(MODE_NOT_AVAILABLE, this.activity);
            }
        };
    }

    /**
     * @return Предоставляет обработчик клика кнопки перехода в режим повторения
     */
    View.OnClickListener getRepetitionModeClickListener() {
        return x -> {
            if (Dictionary.first300WordsDistributed()) {
                Dictionary.changeMode(new RepetitionMode());
                this.activity.fillStatistic();
            } else {
                ToastUtil.showToast(MODE_NOT_AVAILABLE, this.activity);
            }
        };
    }
}