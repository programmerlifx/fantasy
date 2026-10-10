package com.pariaman.fantasy;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.view.*;
import android.widget.Toast;
import java.util.*;

public class MainActivity extends Activity {
  @Override public void onCreate(Bundle b) { super.onCreate(b); getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN ); setContentView(new GameView()); }

  class GameView extends View {
    Canvas c; Paint p=new Paint(); Random random=new Random(4); String page="LOBBY"; int level=12, gold=2480, hp=100, mp=75, exp=63, kills=0; float heroX=.50f, heroY=.65f; long lastTick=System.currentTimeMillis(); boolean attack=false; ArrayList<RectF> buttons=new ArrayList<>(); ArrayList<String> labels=new ArrayList<>();
    int navy=Color.rgb(15,22,39), panel=Color.rgb(26,36,57), goldc=Color.rgb(246,198,91), cream=Color.rgb(255,241,204), muted=Color.rgb(155,174,202), teal=Color.rgb(80,211,178), red=Color.rgb(239,94,104);
    GameView(){super(MainActivity.this); setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
    void color(int color){p.setColor(color);p.setStyle(Paint.Style.FILL);p.setStrokeWidth(1);}
    void rect(float x,float y,float w,float h,int co){color(co);c.drawRect(x,y,x+w,y+h,p);}
    void round(float x,float y,float w,float h,float r,int co){color(co);c.drawRoundRect(x,y,x+w,y+h,r,r,p);}
    void text(String s,float x,float y,float size,int co,boolean bold){color(co);p.setTextSize(size);p.setTypeface(bold?Typeface.create("sans-serif",Typeface.BOLD):Typeface.create("sans-serif",Typeface.NORMAL));c.drawText(s,x,y,p);}
    void center(String s,float x,float y,float size,int co,boolean bold){color(co);p.setTextSize(size);p.setTypeface(bold?Typeface.create("sans-serif",Typeface.BOLD):Typeface.create("sans-serif",Typeface.NORMAL));c.drawText(s,x-p.measureText(s)/2,y,p);}
    @Override protected void onDraw(Canvas canvas){super.onDraw(canvas); c=canvas; float w=getWidth(),h=getHeight(); c.save(); c.scale(w/960f,h/540f); buttons.clear();labels.clear(); drawBackground(); if(page.equals("LOBBY"))drawLobby(); else if(page.equals("BATTLE"))drawBattle(); else drawPanelPage(); c.restore(); postInvalidateDelayed(100); }
    void drawBackground(){rect(0,0,960,540,navy); // sky gradient blocks
      rect(0,0,960,265,Color.rgb(28,46,76)); rect(0,210,960,100,Color.rgb(47,70,91));
      // distant mountains
      for(int i=0;i<10;i++){int x=i*110-30;rect(x,190+(i%3)*15,75,95,Color.rgb(44,64,82));rect(x+12,177+(i%3)*15,50,20,Color.rgb(64,84,96));}
      // pixel clouds
      for(int i=0;i<4;i++){int x=70+i*230+(int)(Math.sin(System.currentTimeMillis()/1800.0+i)*12);rect(x,72+i%2*25,60,12,Color.rgb(95,121,139));rect(x+14,60+i%2*25,32,12,Color.rgb(95,121,139));}
      // ground
      rect(0,300,960,240,Color.rgb(39,76,68));rect(0,318,960,22,Color.rgb(51,96,76));
      for(int i=0;i<30;i++){int x=(i*73)%960,y=350+(i*37)%180;rect(x,y,5,4,Color.rgb(57,102,77));}
      // path to town
      color(Color.rgb(105,93,76)); Path path=new Path();path.moveTo(380,540);path.lineTo(585,540);path.lineTo(530,300);path.lineTo(450,300);path.close();c.drawPath(path,p);
      rect(450,300,80,240,Color.rgb(126,107,82)); for(int y=330;y<540;y+=30)rect(450+(y%3)*3,y,80,3,Color.rgb(143,121,92));
      // house
      rect(720,214,112,102,Color.rgb(126,79,61));rect(705,201,140,24,Color.rgb(71,45,53));rect(721,185,108,19,Color.rgb(91,53,57));rect(739,250,25,30,Color.rgb(52,62,76));rect(789,246,23,26,Color.rgb(244,190,89));
      // tree pixels
      tree(105,260);tree(855,286);tree(650,330);tree(260,350);
    }
    void tree(int x,int y){rect(x+13,y+35,12,42,Color.rgb(101,68,49));rect(x,y+10,40,30,Color.rgb(42,110,78));rect(x+7,y,28,28,Color.rgb(51,133,85));rect(x+12,y+7,8,7,Color.rgb(75,157,93));}
    void hero(float x,float y,boolean moving){int xx=(int)x, yy=(int)y; // pixel hero
      rect(xx-13,yy-1,26,26,Color.rgb(54,76,126));rect(xx-10,yy-10,20,18,Color.rgb(231,179,133));rect(xx-12,yy-15,24,9,Color.rgb(100,54,42));rect(xx-13,yy-17,26,6,goldc);rect(xx-7,yy-5,4,4,Color.rgb(34,43,57));rect(xx+4,yy-5,4,4,Color.rgb(34,43,57));rect(xx-9,yy+24,7,12,Color.rgb(45,47,69));rect(xx+2,yy+24,7,12,Color.rgb(45,47,69));rect(xx+13,yy+1,5,27,Color.rgb(193,207,219));rect(xx+10,yy-3,11,9,Color.rgb(226,231,233)); if(attack){rect(xx+17,yy-17,6,18,Color.WHITE);rect(xx+9,yy-19,18,4,goldc);} }
    void addButton(String label,float x,float y,float w,float h,boolean primary){round(x,y,w,h,10,primary?Color.rgb(116,81,43):panel);round(x+2,y+2,w-4,h-4,8,primary?Color.rgb(173,119,52):Color.rgb(34,46,69));center(label,x+w/2,y+h/2+5,14,primary?cream:Color.rgb(220,230,244),true);buttons.add(new RectF(x,y,x+w,y+h));labels.add(label);}
    void bar(float x,float y,float w,float val,int co){round(x,y,w,8,4,Color.rgb(55,65,83));round(x,y,w*Math.max(0,Math.min(1,val)),8,4,co);}
    void drawTop(){round(18,16,925,65,13,0xDD10192B);text("✦  PROJECT FANTASY",34,43,18,goldc,true);text("ELARA  •  Swordsman",34,65,12,cream,false);text("Lv."+level,340,42,16,cream,true);text("HP",397,35,10,muted,true);bar(425,28,100,hp/100f,red);text(""+hp+"/100",531,36,10,cream,false);text("MP",397,57,10,muted,true);bar(425,50,100,mp/100f,Color.rgb(83,163,239));text(mp+"/100",531,58,10,cream,false);text("◈  "+gold,820,45,16,goldc,true);}
    void drawLobby(){drawTop();hero(482+(float)Math.sin(System.currentTimeMillis()/700.0)*4,325,false);round(24,102,215,122,12,0xDD111A2B);text("WELCOME BACK",40,128,12,muted,true);text("Elara Moonblade",40,154,18,cream,true);text("Swordsman  •  Lv. "+level,40,177,12,goldc,false);text("EXP  "+exp+"%",40,202,11,muted,false);bar(92,194,125,exp/100f,teal);
      round(735,105,200,118,12,0xDD111A2B);text("DAILY QUEST",752,130,12,goldc,true);text("Defeat 5 Slimes",752,154,14,cream,true);text(kills+" / 5 completed",752,176,11,muted,false);bar(752,188,162,Math.min(1,kills/5f),teal);
      round(24,421,912,96,13,0xE611192B);text("TOWN OF LUMEN",42,446,12,goldc,true);text("Choose your next adventure",42,466,12,muted,false);
      addButton("PLAY",42,476,130,31,true);addButton("PROFILE",184,476,105,31,false);addButton("INVENTORY",301,476,115,31,false);addButton("SKILLS",428,476,90,31,false);addButton("QUESTS",530,476,95,31,false);addButton("PET",637,476,72,31,false);addButton("SETTINGS",721,476,105,31,false);addButton("EXIT",838,476,75,31,false);
      text("v1.0  •  OFFLINE ADVENTURE",735,409,10,cream,true);
    }
    void drawBattle(){drawTop(); // enemies
      for(int i=0;i<3;i++){int x=300+i*150,y=320+(i%2)*18;rect(x-15,y-5,30,24,Color.rgb(88,166,110));rect(x-10,y-14,20,13,Color.rgb(112,197,129));rect(x-7,y-9,4,4,Color.rgb(19,40,35));rect(x+3,y-9,4,4,Color.rgb(19,40,35));bar(x-23,y-25,46,Math.max(.1f,1-kills*.12f),red);center("SLIME",x,y+33,9,cream,true);}
      hero(heroX*960,heroY*540,attack);round(20,420,920,100,13,0xE611192B);text("FOREST OUTSKIRTS",38,444,12,goldc,true);text("Tap ATTACK to fight • Move with arrows",38,464,12,muted,false);addButton("◀",38,474,55,32,false);addButton("▲",99,474,55,32,false);addButton("▼",160,474,55,32,false);addButton("ATTACK ⚔",665,464,130,43,true);addButton("SKILL ✦",806,464,115,43,false);addButton("LOBBY",510,474,100,32,false);
    }
    void drawPanelPage(){drawTop();round(150,105,660,365,18,0xF1111A2B);String title=page;center(title,480,145,25,goldc,true); if(page.equals("PROFILE")){hero(330,220,false);text("ELARA MOONBLADE",390,190,18,cream,true);text("Class: Swordsman",390,220,14,muted,false);text("Level: "+level,390,246,14,cream,false);text("Strength      18",390,278,13,cream,false);text("Agility         12",390,302,13,cream,false);text("Vitality        16",390,326,13,cream,false);text("Adventure rank: Bronze III",390,360,13,goldc,false);} else if(page.equals("INVENTORY")){String[] items={"Iron Sword  +4 ATK","Traveler Armor  +3 DEF","Red Potion  x5","Blue Potion  x3","Lumen Ring  +2 VIT","Slime Jelly  x8"};for(int i=0;i<items.length;i++){int x=190+(i%2)*290,y=177+(i/2)*70;round(x,y,260,55,9,Color.rgb(38,51,75));rect(x+12,y+12,30,30,new int[]{goldc,0xff6f91bc,red,0xff65a9e9,0xffb69be8,teal}[i]);text(items[i],x+53,y+32,13,cream,true);}} else if(page.equals("SKILLS")){text("BASIC ATTACK",205,205,16,cream,true);text("Quick sword strike. Reliable damage.",205,229,13,muted,false);text("POWER SLASH  •  MP 20",205,275,16,cream,true);text("A heavy slash that hits nearby enemies.",205,299,13,muted,false);text("IRON WILL  •  PASSIVE",205,345,16,cream,true);text("Increase defense while exploring.",205,369,13,muted,false);} else if(page.equals("QUESTS")){text("DAILY: Slime Cleanup",205,205,16,cream,true);text("Defeat 5 slimes in the forest.",205,231,13,muted,false);bar(205,246,420,Math.min(1,kills/5f),teal);text(kills+" / 5   •   Reward: 250 Gold + 80 EXP",205,273,13,goldc,false);text("STORY: The Missing Merchant",205,320,16,cream,true);text("Find the merchant near the old bridge.",205,346,13,muted,false);} else if(page.equals("PET")){center("PIXEL FOX COMPANION",480,215,18,cream,true); // fox sprite
        rect(440,248,80,55,0xffc57c4a);rect(452,233,22,24,0xffd99b60);rect(486,233,22,24,0xffd99b60);rect(455,247,7,7,Color.BLACK);rect(492,247,7,7,Color.BLACK);rect(468,262,15,8,0xffffdfb0);center("Mochi  •  Bond Lv. 3",480,330,14,goldc,true);center("Provides a little luck while adventuring.",480,355,12,muted,false);} else {center("GAME SETTINGS",480,215,18,cream,true);center("Sound: ON     •     Effects: ON",480,255,14,muted,false);center("This demo saves progress during this session.",480,285,12,muted,false);}
      addButton("BACK TO LOBBY",385,414,190,38,true);
    }
    @Override public boolean onTouchEvent(android.view.MotionEvent e){if(e.getAction()!=MotionEvent.ACTION_UP)return true;float x=e.getX()*960/getWidth(),y=e.getY()*540/getHeight();for(int i=0;i<buttons.size();i++){if(buttons.get(i).contains(x,y)){String b=labels.get(i);handle(b);invalidate();return true;}}return true;}
    void handle(String b){if(b.equals("PLAY")){page="BATTLE";heroX=.52f;heroY=.66f;}else if(b.equals("PROFILE")||b.equals("INVENTORY")||b.equals("SKILLS")||b.equals("QUESTS")||b.equals("PET")||b.equals("SETTINGS")){page=b;}else if(b.equals("BACK TO LOBBY")||b.equals("LOBBY")){page="LOBBY";attack=false;}else if(b.equals("ATTACK ⚔")){attack=true;kills++;gold+=50;exp+=7;if(exp>=100){exp-=100;level++;}hp=Math.max(50,hp-2);mp=Math.max(0,mp-1);Toast.makeText(MainActivity.this,"Critical hit! +50 Gold, +7 EXP",Toast.LENGTH_SHORT).show();new android.os.Handler().postDelayed(()->{attack=false;invalidate();},250);}else if(b.equals("SKILL ✦")){if(mp>=20){mp-=20;kills+=2;gold+=100;Toast.makeText(MainActivity.this,"Power Slash! +100 Gold",Toast.LENGTH_SHORT).show();}else Toast.makeText(MainActivity.this,"MP tidak cukup",Toast.LENGTH_SHORT).show();}else if(b.equals("◀")){heroX=Math.max(.24f,heroX-.06f);}else if(b.equals("▲")){heroY=Math.max(.47f,heroY-.05f);}else if(b.equals("▼")){heroY=Math.min(.75f,heroY+.05f);}else if(b.equals("EXIT")){page="SETTINGS";}}
  }
}
