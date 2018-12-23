package io.left.core.assignment.ui.fuzzylogic;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.fuzzylite.Engine;
import com.fuzzylite.FuzzyLite;
import com.fuzzylite.Op;
import com.fuzzylite.activation.General;
import com.fuzzylite.defuzzifier.Centroid;
import com.fuzzylite.norm.s.Maximum;
import com.fuzzylite.norm.t.AlgebraicProduct;
import com.fuzzylite.norm.t.Minimum;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.Ramp;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;

import io.left.core.util.R;

public class HeartDiseaseActivity extends AppCompatActivity {

    Button diagnosis,reset;
    EditText age,bloodPressure,cholesterol,bloodSugar;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_disease);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        age=findViewById(R.id.age);
        bloodPressure=findViewById(R.id.bloodPressure);
        cholesterol=findViewById(R.id.cholesterol);
        bloodSugar=findViewById(R.id.bloodSugar);
        diagnosis=findViewById(R.id.diagnosis);
        reset=findViewById(R.id.reset);
        result=findViewById(R.id.result);


        diagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ageUser = age.getText().toString().trim();
                String bloodPressureUser = bloodPressure.getText().toString().trim();
                String cholesterolUSer = cholesterol.getText().toString().trim();
                String bloodSugarUser = bloodSugar.getText().toString().trim();

                if (ageUser.isEmpty()||bloodPressureUser.isEmpty()||cholesterolUSer.isEmpty()||bloodSugarUser.isEmpty() ){
                    Toast.makeText(HeartDiseaseActivity.this, "Please enter information", Toast.LENGTH_LONG).show();
                }else {
                    setEngineAndGetFuzzyResult(Double.parseDouble(ageUser),
                            Double.parseDouble(bloodPressureUser),
                            Double.parseDouble(cholesterolUSer),
                            Double.parseDouble(bloodSugarUser));
                }

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReset();
            }
        });

    }


    private void setReset(){
        age.setText("");
        bloodPressure.setText("");
        cholesterol.setText("");
        bloodSugar.setText("");
    }


    public  void setEngineAndGetFuzzyResult(double userAge,double  userBloodPressure,double userCholesterol,double userBloodSugar){
        Engine engine = new Engine();
        engine.setName("Heart disease Diagnosis System");
        engine.setDescription("It will give the disease condition");

        InputVariable age  = new InputVariable();
        age.setName("p");
        age.setDescription("");
        age.setEnabled(true);
        age.setRange(0.000, 150.000);
        age.setLockValueInRange(true);
        age.addTerm(new Ramp("Y", 0.000, 38.000));
        age.addTerm(new Ramp("MI", 35.000, 50.000));
        age.addTerm(new Ramp("O", 45.000, 65.000));
        age.addTerm(new Ramp("VO", 60.000, 150.000));
        engine.addInputVariable(age);



        InputVariable bloodPressure   = new InputVariable();
        bloodPressure.setName("q");
        bloodPressure.setDescription("");
        bloodPressure.setEnabled(true);
        bloodPressure.setRange(0.000, 300.000);
        bloodPressure.setLockValueInRange(true);
        bloodPressure.addTerm(new Ramp("L", -15.000, 125.000));
        bloodPressure.addTerm(new Ramp("MD", 120.000, 145.000));
        bloodPressure.addTerm(new Ramp("H", 140.000, 165.000));
        bloodPressure.addTerm(new Ramp("VH", 160.000, 300.000));
        engine.addInputVariable(bloodPressure);


        InputVariable cholesterol   = new InputVariable();
        cholesterol.setName("r");
        cholesterol.setDescription("");
        cholesterol.setEnabled(true);
        cholesterol.setRange(0.000, 300.000);
        cholesterol.setLockValueInRange(true);
        cholesterol.addTerm(new Ramp("L", 0.000, 200.000));
        cholesterol.addTerm(new Ramp("MD", 190.000, 240.000));
        cholesterol.addTerm(new Ramp("H", 235.000, 300.000));
        engine.addInputVariable(cholesterol);

        InputVariable bloodSugar   = new InputVariable();
        bloodSugar.setName("s");
        bloodSugar.setDescription("");
        bloodSugar.setEnabled(true);
        bloodSugar.setRange(0.000, 300.000);
        bloodSugar.setLockValueInRange(true);
        bloodSugar.addTerm(new Ramp("L", -15.000, 200.000));
        bloodSugar.addTerm(new Ramp("H", 110.000, 300.000));
        engine.addInputVariable(bloodSugar);

        OutputVariable output = new OutputVariable();
        output.setName("t");
        output.setDescription("");
        output.setEnabled(true);
        output.setRange(0.000, 4.000);
        output.setLockValueInRange(true);
        output.setAggregation(new Maximum());
        output.setDefuzzifier(new Centroid(100));
        output.setDefaultValue(Double.NaN);
        output.setLockPreviousValue(false);
        output.addTerm(new Ramp("HL", 0.000, 1.600));
        output.addTerm(new Ramp("MD", 1.500, 2.600));
        output.addTerm(new Ramp("SC", 2.500, 4.000));
        engine.addOutputVariable(output);

        RuleBlock mamdani = new RuleBlock();
        mamdani.setName("mamdani");
        mamdani.setDescription("");
        mamdani.setEnabled(true);
        mamdani.setConjunction(new Minimum());
        mamdani.setDisjunction(new Maximum());
        mamdani.setImplication(new Minimum());
        mamdani.setActivation(new General());
        mamdani.addRule(Rule.parse("if p is Y and q is L and r is L and s is L then t is HL", engine));
        mamdani.addRule(Rule.parse("if p is Y and q is L and r is L and s is H then t is MD", engine));
        mamdani.addRule(Rule.parse("if p is Y and q is MD and r is H and s is H then t is SC", engine));
        mamdani.addRule(Rule.parse("if p is MI and q is L and r is L and s is L then t is HL", engine));
        mamdani.addRule(Rule.parse("if p is MI and q is L and r is MD and s is H then t is MD", engine));
        mamdani.addRule(Rule.parse("if p is MI and q is L and r is H and s is H then t is SC", engine));
        mamdani.addRule(Rule.parse("if p is O and q is L and r is L and s is L then t is HL", engine));
        mamdani.addRule(Rule.parse("if p is O and q is L and r is L and s is H then t is MD", engine));
        mamdani.addRule(Rule.parse("if p is O and q is MD and r is H and s is H then t is SC", engine));
        mamdani.addRule(Rule.parse("if p is VO and q is L and r is L and s is L then t is HL", engine));
        mamdani.addRule(Rule.parse("if p is VO and q is L and r is H and s is H then t is MD", engine));
        mamdani.addRule(Rule.parse("if p is VO and q is MD and r is L and s is H then t is SC", engine));
        engine.addRuleBlock(mamdani);



        StringBuilder status = new StringBuilder();
        if (! engine.isReady(status))
            throw new RuntimeException("[engine error] engine is not ready:n" + status);

        InputVariable ageInput = engine.getInputVariable("p");
        InputVariable bloodPressureInput = engine.getInputVariable("q");
        InputVariable cholesterolInput = engine.getInputVariable("r");
        InputVariable bloodSugarInput = engine.getInputVariable("s");
        OutputVariable disease = engine.getOutputVariable("t");

        ageInput.setValue(userAge);
        bloodPressureInput.setValue(userBloodPressure);

        cholesterolInput.setValue(userCholesterol);
        bloodSugarInput.setValue(userBloodSugar);

        engine.process();

        String resultDisease[]=disease.fuzzyOutputValue().split(" ");
        result.setText(resultDisease[0]);




    }

}
