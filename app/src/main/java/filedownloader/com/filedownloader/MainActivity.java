package filedownloader.com.filedownloader;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.filedownloader.FileDownloader;
import com.filedownloader.listener.OnStartListener;
import com.filedownloader.listener.OnSuccessListener;

import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import filedownloader.com.filedownloader.utils.AlertUtil;
import filedownloader.com.filedownloader.utils.DirectoryUtil;
import filedownloader.com.filedownloader.utils.StringUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.editTextUrl)
    EditText editTextUrl;
    @BindView(R.id.buttonDownload)
    Button buttonDownload;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.textViewProgress)
    TextView textViewProgress;
    @BindView(R.id.textViewProgressPercent)
    TextView textViewProgressPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        editTextUrl.setText("http://212.183.159.230/10MB.zip");
        editTextUrl.setText("https://www.sample-videos.com/zip/100mb.zip");

        buttonDownload.setOnClickListener(view -> {
            String url = editTextUrl.getText().toString();
            if (!TextUtils.isEmpty(url)) {
                String path = DirectoryUtil.createDirectory(this.getExternalFilesDir("downloaded")) + File.separator + StringUtil.getFileNameFromUrl(url);
                File outputFile = new File(path);
                new FileDownloader.Builder(this)
                        .url(url)
                        .outputFile(outputFile)
                        .listener((OnStartListener) () -> {
                            progressBar.setIndeterminate(true);
                            textViewProgressPercent.setText("0%");
                            textViewProgress.setText("");
                        })
                        .listener((bytesWritten, totalSize) -> {
                            progressBar.setIndeterminate(false);
                            progressBar.setMax((int) totalSize);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                progressBar.setProgress((int) bytesWritten, true);
                            } else {
                                progressBar.setProgress((int) bytesWritten);
                            }
                            textViewProgressPercent.setText(String.format(Locale.getDefault(), "%.0f%%", (totalSize > 0) ? (bytesWritten * 1.0 / totalSize) * 100 : -1));
                            textViewProgress.setText(String.format(Locale.getDefault(), "%s/%s", StringUtil.bytes2String(bytesWritten), StringUtil.bytes2String(totalSize)));
                        })
                        .listener((OnSuccessListener) file -> {
                            new AlertUtil.Builder(this)
                                    .setTitle("File Downloaded!")
                                    .setMessage(String.format(Locale.getDefault(), "Name : %s\nPath : %s", file.getName(), file.getAbsolutePath()))
                                    .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                                    .create()
                                    .show();
                        })
                        .build()
                        .start();
            }
        });

    }
}
