# androidNotes

//button, textbox, lable

Button scanButton;
EditText editText;
TextView textView;

scanButton = (Button) findViewById(R.id.resetCartButton);
textView = (TextView) findViewById(R.id.budgetTextView);
editText = (EditText) findViewById(R.id.budgetEditText);


textView.setTextColor(Color.parseColor("#EF6C00"));
saveButton.setEnabled(true);

confirmBudgetButton.setOnClickListener(new View.OnClickListener() {
 @Override
 public void onClick(View v) {
  String budget = editText.getText().toString();
  if (budget.length() > 0) {
   if (Integer.parseInt(budget) > 1 && Integer.parseInt(budget) < 1000000) {}
   textView.setText("hello " + budget);
  }
 }
});

//loading
ProgressBar progressBar;
progressBar = (ProgressBar) findViewById(R.id.progressBar22);
progressBar.setVisibility(View.INVISIBLE);

//Toasts
Toast.makeText(getApplicationContext(), "Reset Successful", Toast.LENGTH_SHORT).show();

//intents
Intent intent = new Intent(getApplicationContext(), MainActivity.class);
intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
startActivity(intent);

//shared pref: read
SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
sharedPreferences.getString("budget", null //default value
);

//shared pref: write
SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
SharedPreferences.Editor editor = sharedPreferences.edit();
editor.putString("budget", budget);
editor.commit();

//Gson: from JSON- decoding from string to original format
Gson gson = new Gson();
Type type = new TypeToken < ArrayList < MemoData >> () {}.getType();
String json = sharedPreferences.getString("memoList", null);
ArrayList < MemoData > arrayList = gson.fromJson(json, type);

//Gson: to JSON- encoding from original format to string
			//ArrayList<MemoData> arrayList ;
Gson gson = new Gson();
String json2 = gson.toJson(arrayList); //json to be saved


//Firebase scan
FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
DatabaseReference databaseReference = firebaseDatabase.getReference("allProducts");
databaseReference.addValueEventListener(new ValueEventListener() {
 @Override
 public void onDataChange(DataSnapshot dataSnapshot) {
  dataSnapshot.child("productList").getValue();
  for (int i = 0; i < n; i++) {
   dataSnapshot.child("productList").child(i + "").child("productId").getValue().toString();
  }
 }
 @Override
 public void onCancelled(DatabaseError databaseError) {

 }
});
