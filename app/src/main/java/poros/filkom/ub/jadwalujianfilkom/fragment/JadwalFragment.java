package poros.filkom.ub.jadwalujianfilkom.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import poros.filkom.ub.jadwalujianfilkom.R;
import poros.filkom.ub.jadwalujianfilkom.adapter.JadwalAdapter;
import poros.filkom.ub.jadwalujianfilkom.model.Cell;
import poros.filkom.ub.jadwalujianfilkom.model.DetailJadwal;
import poros.filkom.ub.jadwalujianfilkom.model.JadwalResponse;
import poros.filkom.ub.jadwalujianfilkom.network.ApiGenerator;
import poros.filkom.ub.jadwalujianfilkom.network.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class JadwalFragment extends Fragment {

    public static String TAG= JadwalFragment.class.getSimpleName();

    private String TAGS = "Hm3";

    private RecyclerView recyclerView;
    private JadwalAdapter jadwalAdapter;
    private static ArrayList<DetailJadwal> jadwalKu =  new ArrayList<>();
    private static ArrayList<String> jadwalDouble = new ArrayList<>();

    String jadwalUts = "";
    static String ruangan = "";
    static String prodi = "";
    private String[] daftarHari = {"Senin, 08 Okt", "Selasa, 09 Okt", "Rabu, 10 Okt", "Kamis, 11 Okt", "Jumat, 12 Okt", "Senin, 15 Okt", "Selasa, 16 Okt", "Rabu, 17 Okt", "Kamis, 18 Okt", "Jumat, 19 Okt"};

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    private static ArrayList<DetailJadwal> jadwals;

    public JadwalFragment() {
    }

    public static JadwalFragment newInstance() {
        JadwalFragment fragment = new JadwalFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBundle();
        jadwalAdapter = new JadwalAdapter(getContext());


        sharedPreferences = getContext().getSharedPreferences("jadwal", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("offline", false)) {
            //Toast.makeText(getContext(), "off", Toast.LENGTH_SHORT).show();
            getJadwalOff();
            //getJadwal();
        } else {
            //Toast.makeText(getContext(), "on", Toast.LENGTH_SHORT).show();
            getJadwal();
            jadwalAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_jadwal, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initSharedPref();
        initRecyclerView();

        getJadwalKu();
    }

    private void initBundle() {
        Intent intent = getActivity().getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        jadwals = (ArrayList<DetailJadwal>) args.getSerializable("ARRAYLIST");
    }

    private void initSharedPref() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        jadwalAdapter = new JadwalAdapter(getContext());
    }

    private void initRecyclerView() {
        sharedPreferences = getContext().getSharedPreferences("jadwal", MODE_PRIVATE);
        prodi = sharedPreferences.getString("prodi", "no prodi");
    }

    private void getJadwalOff() {
        jadwalKu.clear();
        sharedPreferences = getContext().getSharedPreferences("jadwal", MODE_PRIVATE);
        String jadwalUas = sharedPreferences.getString("jadwalUas", "");

        Gson gson = new Gson();
        //jadwalUas = gson.toJson(jadwalUas);
        //jadwalUas = jadwalUas.substring(1, jadwalUas.length()-1);
        Log.d(TAG, "getJadwalOff: "+jadwalUas);
        Log.d(TAG, "getJadwalOff: "+jadwalUas);
        JadwalResponse jadwalResponse = gson.fromJson(jadwalUas, JadwalResponse.class);
        Log.d(TAG, "gaaetJadwalOff: aa"+jadwalResponse.getPages().get(0).getTables().get(0).getCells().get(0).getI());
        //loadJadwal(jadwalResponse);

    }

    private void getJadwal() {
        Service service = ApiGenerator.createService(Service.class);

        String daySplitter = "E1.1";
        int count = 0;
        int countLine = 0;

        String dataUts[][] = new String[11][300];
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getContext().getAssets().open("rabu.txt")));

            // do reading, usually loop until end of file reading
            String lines;
            while ((lines = reader.readLine()) != null) {
                //process line
                String column[] = lines.split("@@");
                if (column[0].trim().equals(daySplitter)) {
                    count++;
                    countLine = 0;
                    Log.d(TAG, "getJadwal: ==============="+count);
                }
                dataUts[count][countLine] = lines;
                Log.d(TAG, "getJadwal: linee"+lines);
                countLine++;
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        count = 0;
        countLine = 0;

        jadwalKu.clear();

        prodi = sharedPreferences.getString("prodi", "no prodi");

        switch (prodi) {
            case "Magister Ilmu Komputer":
                prodi = "MIK";
                break;
            case "Teknik Informatika":
                prodi = "TIF";
                break;
            case "Teknik Komputer":
                prodi = "TEKOM";
                break;
            case "Pendidikan Teknologi Informasi":
                prodi = "PTI";
                break;
            case "Sistem Informasi":
                prodi = "SI";
                break;
            case "Teknologi Informasi":
                prodi = "TI";
                break;
        }


        for (int i = 0; i < jadwals.size(); i++) {
            Log.d(TAG, "getJadwal: jadwalsku"+jadwals.get(i).getMatkul());
            Log.d(TAG, "getJadwal: jadwalsku"+jadwals.get(i).getKelas());
        }

        Log.d(TAG, "uts prodi " + prodi );
        Log.d(TAG, "getJadwal: totalJadwal"+jadwals.size());
        int ke = 0;
        for (int x = 1; x < 11; x ++) {

            Log.d(TAG, "uts " + x );
            for (int i = 0; i < dataUts[x].length; i++) {
                //for (int j = 1; j < 11; j++) {
                if (dataUts[x][i] != null) {
                    //Log.d(TAG, "uts senin" + x + " " + dataUts[x][i].trim());

                    String column[] = dataUts[x][i].split("@@");
                    if (column[0].trim().equals(daySplitter)) {
                        count++;
                        countLine = 0;
                        Log.d(TAG, "uts getJadwal: ===============" + count);
                    }

                    for (int l = 0; l < jadwals.size(); l++) {

                        String matkul = jadwals.get(l).getMatkul().trim();
                        String kelas = jadwals.get(l).getKelas().trim();
                        Log.d(TAG, "uts p" + x + " " + prodi);

                        if (column[5].trim().equals("Administrasi Sistem Server")) {
                            Log.d(TAG, "getJadwal: lkajsdflkasjdflasdkjf"+column[5].trim() + column[6].trim());
                        }

                        if (prodi.equals(column[1].trim()) && matkul.equals(column[2].trim()) && kelas.equals(column[3].trim())) {
                            Log.d(TAG, "uts " + x + " " + column[1].trim());
                            Log.d(TAG, "uts " + x + " " + column[2].trim());
                            Log.d(TAG, "uts " + x + " " + column[3].trim());

                            Log.d(TAG, "getJadwal: daysenin");

                            DetailJadwal detailJadwal = new DetailJadwal();
                            detailJadwal.setHari(daftarHari[count-1]);
                            detailJadwal.setJam("07.30 - 09.30");
                            detailJadwal.setKelas(kelas);
                            detailJadwal.setMatkul(matkul);
                            detailJadwal.setRuang(column[0].trim());
                            jadwalKu.add(ke, detailJadwal);
                            ke++;
                            break;
                        } else

                        if (prodi.equals(column[4].trim()) && matkul.equals(column[5].trim()) && kelas.equals(column[6].trim())) {
                            Log.d(TAG, "getJadwal: prodd"+prodi);
                            Log.d(TAG, "getJadwal: prodduts"+column[4].trim());
                            Log.d(TAG, "getJadwal: proddu============");
                            Log.d(TAG, "uts " + x + " " + column[4].trim());
                            Log.d(TAG, "uts " + x + " " + column[5].trim());
                            Log.d(TAG, "uts " + x + " " + column[6].trim());

                            DetailJadwal detailJadwal = new DetailJadwal();
                            detailJadwal.setHari(daftarHari[count-1]);
                            detailJadwal.setJam("09.30 - 11.30");
                            detailJadwal.setKelas(kelas);
                            detailJadwal.setMatkul(matkul);
                            detailJadwal.setRuang(column[0].trim());
                            jadwalKu.add(ke, detailJadwal);
                            ke++;
                            break;
                        } else

                        if (prodi.equals(column[7].trim()) && matkul.equals(column[8].trim()) && kelas.equals(column[9].trim())) {
                            Log.d(TAG, "uts " + x + " " + column[7].trim());
                            Log.d(TAG, "uts " + x + " " + column[8].trim());
                            Log.d(TAG, "uts " + x + " " + column[9].trim());

                            DetailJadwal detailJadwal = new DetailJadwal();
                            detailJadwal.setHari(daftarHari[count-1]);
                            detailJadwal.setJam("13.00 - 15.00");
                            detailJadwal.setKelas(kelas);
                            detailJadwal.setMatkul(matkul);
                            detailJadwal.setRuang(column[0].trim());
                            jadwalKu.add(ke, detailJadwal);
                            ke++;
                            break;
                        } else {
//                                                            Log.d(TAG, "uts "+matkul + kelas);
//                            Log.d(TAG, "getJadwal: uts"+column[2].trim() + column[3].trim());
                        }

                    }


                }
                //}
            }
        }
        Log.d(TAG, "getJadwal: total ke:"+ke);

        Call<JadwalResponse> call = service.getJadwalUAS();
        //jadwalKu.clear();

        call.enqueue(new Callback<JadwalResponse>() {
            @Override
            public void onResponse(Call<JadwalResponse> call, Response<JadwalResponse> response) {

                Gson gson = new Gson();
                String jsonString = gson.toJson(response.body());

                sharedPreferences = getContext().getSharedPreferences("jadwal", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("jadwalUas", jsonString);
                editor.apply();

                JadwalResponse jadwalResponse = gson.fromJson(jsonString , JadwalResponse.class);
                Log.d(TAG, "onResponse: jadwal2"+jadwalResponse.getPages().get(0).getNumber().toString());

                //JadwalResponse jadwalResponse = response.body();
                //loadJadwal(jadwalResponse);
                jadwalAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<JadwalResponse> call, Throwable t) {
                Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getJadwalKu() {
        for (int i = 0; i < jadwalKu.size(); i++) {
            Log.d(TAGS, "getJadwalKu: ==========================");
            Log.d(TAGS, "getJadwalKu: "+jadwalKu.get(i).getHari());
            Log.d(TAGS, "getJadwalKu: "+jadwalKu.get(i).getJam());
            Log.d(TAGS, "getJadwalKu: "+jadwalKu.get(i).getKelas());
            Log.d(TAGS, "getJadwalKu: "+jadwalKu.get(i).getMatkul());
            Log.d(TAGS, "getJadwalKu: "+jadwalKu.get(i).getRuang());
        }
        jadwalAdapter = new JadwalAdapter(getContext());
        jadwalAdapter.addItem(jadwalKu);
        recyclerView.setAdapter(jadwalAdapter);
    }
}
