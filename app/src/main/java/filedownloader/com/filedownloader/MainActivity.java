package filedownloader.com.filedownloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.filedownloader.FileDownloader;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        editTextUrl.setText("https://www.google.com.sg/logos/doodles/2018/world-cup-2018-day-14-6046718054367232-5721036024709120-ssw.png");

        buttonDownload.setOnClickListener(view -> {
            String url = editTextUrl.getText().toString();
            if (!TextUtils.isEmpty(url)) {
                String path = DirectoryUtil.createDirectory(this.getExternalFilesDir("downloaded")) + File.separator + StringUtil.getFileNameFromUrl(url);
                File outputFile = new File(path);
                new FileDownloader.Builder(this)
                        .url(url)
                        .outputFile(outputFile)
                        .listener((bytesWritten, totalSize) -> Log.d(TAG, StringUtil.bytes2String(bytesWritten) + "/" + StringUtil.bytes2String(totalSize)))
                        .listener((OnSuccessListener) file -> {
                            new AlertUtil.Builder(this)
                                    .setTitle("File Downloaded!")
                                    .setMessage(String.format(Locale.getDefault(), "Name : %s\nPath : %s", file.getName(), file.getAbsolutePath()))
                                    .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                                    .create()
                                    .show();
                        })
                        .build();
            }
        });

    }
}
