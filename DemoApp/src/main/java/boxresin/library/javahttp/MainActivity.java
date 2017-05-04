package boxresin.library.javahttp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;

import boxresin.library.javahttp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
{
	private ActivityMainBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

		if (savedInstanceState != null)
		{
			String response = savedInstanceState.getString("response");
			if (response != null)
				binding.txtResponse.setText(response);
		}

		try
		{
			HttpResponse response = new HttpRequester()
					.setUrl("https://www.google.com/")
					.setMethod("GET")
					.addHeader("key", "value")
					.addHeader("key", "value")
					.addHeader("key", "value")
					.addHeader("key", "value")
					.request();
		}
		catch (IOException e)
		{
		}
	}

	public void onClick(View view)
	{
		if (binding.btnRequest == view)
		{
			final String url = binding.editUrl.getText().toString();
			final String method = (String) binding.spinner.getSelectedItem();

			binding.btnRequest.setEnabled(false);
			binding.loading.setVisibility(View.VISIBLE);
			binding.txtResponse.setText("");

			new Thread()
			{
				@Override public void run()
				{
					try
					{
						final HttpResponse response = new HttpRequester()
								.setUrl(url)
								.setMethod(method)
								.request();

						runOnUiThread(new Runnable()
						{
							@Override public void run()
							{
								binding.btnRequest.setEnabled(true);
								binding.loading.setVisibility(View.GONE);
								binding.txtResponse.setText(response.getBody());
							}
						});

						int statusCode = response.getStatusCode();
						String statusMessage = response.getStatusMessage();
						String body = response.getBody();
						String header = response.getHeader("something");
					}
					catch (IOException e)
					{
					}
				}
			}.start();
		}
	}

	@Override protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putString("response", binding.txtResponse.getText().toString());
	}
}
