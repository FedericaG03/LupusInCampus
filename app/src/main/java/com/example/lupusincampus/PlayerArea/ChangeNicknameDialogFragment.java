package com.example.lupusincampus.PlayerArea;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;

public class ChangeNicknameDialogFragment extends DialogFragment {

    private OnNicknameChangeListener listener;

    public interface OnNicknameChangeListener {
        void onNicknameChanged(String newNickname);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EditText input = new EditText(getActivity());
        input.setHint("Inserisci nuovo nickname");

        return new AlertDialog.Builder(getActivity())
                .setTitle("Cambia Nickname")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newNickname = input.getText().toString().trim();
                    if (listener != null && !newNickname.isEmpty()) {
                        listener.onNicknameChanged(newNickname);
                    } else {
                        // Visualizza un messaggio di errore (puoi anche usare un Toast)
                    }
                })
                .setNegativeButton("Annulla", (dialog, which) -> dismiss())
                .create();
    }

    public void setOnNicknameChangeListener(OnNicknameChangeListener listener) {
        this.listener = listener;
    }
}
