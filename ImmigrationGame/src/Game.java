import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import javalib.impworld.WorldScene;
import javalib.worldimages.AboveImage;
import javalib.worldimages.BesideImage;
import javalib.worldimages.FromFileImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;
import tester.Tester;


class ImmigrationGame  extends javalib.impworld.World {
  int width;
  int height;
  int widthXY;
  int heightXY;
  WorldImage startScreen;
  int screenNum;
  boolean firstAction;
  boolean aMerchant;
  boolean bMerchant;
  int merchantDebt;
  int timer;
  boolean startTimer;
  boolean startSifting;
  int numCycleSifting;
  boolean gotGold;
  int money;
  int totalMoney;
  int actionValue;
  int phungryCount;
  int fhungryCount;
  int days;
  boolean paidMerchant;
  boolean paidFamily;
  boolean paidFood;
  boolean paidToday;
  int hp;
  boolean jumped;
  boolean win;
  
  ImmigrationGame() {
    this.startScreen = new FromFileImage("StartingScreen.png");
    this.width = (int) this.startScreen.getWidth();
    this.height = (int) this.startScreen.getHeight();
    this.widthXY = this.width/2;
    this.heightXY = this.height/2;
    this.screenNum = 0;
    // True when you must first perform an action and can't skip screen
    this.firstAction = false;
    this.aMerchant = false;
    this.bMerchant = false;
    this.timer = 0;
    this.startTimer = false;
    this.startSifting = false;
    this.numCycleSifting = 0;
    this.gotGold = false;
    this.money = 0;
    this.totalMoney = 499;
    this.actionValue = 0;
    this.merchantDebt = 150;
    this.phungryCount = 0;
    this.fhungryCount = 0;
    this.days = 0;
    this.paidMerchant = false;
    this.paidFamily = false;;
    this.paidFood = false;
    this.paidToday = false;
    this.hp = 6;
    this.jumped = false;
    this.win = false;
  }
  
  public void onTick() {
    if(this.startTimer) {
      this.timer += 1;
    } else {
      this.timer = 0;
    }
    
    if(this.timer >= 10 && (!this.aMerchant && !this.bMerchant)) {
      System.out.println("GO TO LAST SCENE");
      this.endOfWorld("You choose not to go to America and your family eventually starved to death");
    }
  }
  
  public void onKeyEvent(String event) {
    if(!this.firstAction) {
      if(event.equals("p")) {
        if(this.screenNum == 6) {
          this.jumped = false;
          
          if(this.merchantDebt <= 0 || this.aMerchant || !this.paidMerchant ) {
            this.hp -= 2;
          }
          
          if((!this.paidMerchant || this.merchantDebt <= 0) || this.aMerchant) {
            this.totalMoney /= 2;
          }
          this.screenNum = 3;
        }
        if(this.screenNum == 5) {
          this.money = 0;
          if(this.jumped) {
            this.screenNum += 1;
          } else {
            this.screenNum = 4;
          }
          
        } else {
          this.screenNum += 1;
        }
        
      }
      
      if(event.equals(".....") && this.screenNum == 5 && !this.paidToday) {
        if( this.days % 30 == 0 && this.days != 0) {
          if(this.days > 60) {
            this.totalMoney -= 4;
          } else {
            this.totalMoney -= 20;
          }
        }
        
        if(this.merchantDebt != 0) {
          if(this.totalMoney >= 2) {
            this.paidMerchant = true;
            this.totalMoney -= 2;
            this.merchantDebt -= 2;   
          } else {
            this.jumped = true;
          }
        } else {
          this.paidMerchant = true;
        }
        if(this.totalMoney >= 2) {
          this.paidFamily = true;
          this.totalMoney -= 2;
          this.fhungryCount = 0;
        } else {
          this.fhungryCount += 1;
          
        }
        if(this.totalMoney >= 2) {
          this.paidFood = true;
          this.totalMoney -= 2;
          this.phungryCount = 0;
          this.hp += 1;
        } else {
          this.phungryCount += 1;
        }
        this.paidToday = true;
        
        if((int)(Math.random() * 500) + 1  <= this.totalMoney) {
          this.jumped = true;
        }
       
      }
    } else {
      
      if((!this.aMerchant && !this.bMerchant) && this.screenNum == 2) {
        if (event.equals("a")){
          this.aMerchant = true;
          this.firstAction  = false;
          this.startTimer = false;
          this.onKeyEvent("p");
        } else if (event.equals("b")){
          this.bMerchant = true;
          this.firstAction  = false;
          this.startTimer = false;
          this.onKeyEvent("p");
        }
      }
      
      if(this.actionValue <= 40) {
        if(event.equals("s") && this.screenNum == 4) {
          this.startTimer = true;
          this.startSifting = true;
          this.gotGold = false;
          this.actionValue += 3;
        }
        
        if(event.equals("n") && this.screenNum == 4) {
          this.numCycleSifting = 0;
          this.gotGold = false;
          this.actionValue += 5;
        } 
      }
      
      if(event.equals("e") && this.screenNum == 4) {
        
        this.numCycleSifting = 0;
        this.actionValue = 0;
        this.screenNum += 1;
        this.firstAction = false;
        this.days += 1;
        
        this.paidMerchant = false;
        this.paidFamily = false;
        this.paidFood = false;
        this.paidToday = false;
      }
    }



  }
  
  public WorldScene makeScene() {
    WorldScene s = new WorldScene( this.width, this.height);
    //Make it the ending screen
    WorldImage curImage = this.startScreen;
    
    if(this.screenNum == 0) {
      
      curImage = this.startScreen;
      
    } else if (this.screenNum == 1){
      
      curImage = new FromFileImage("Screen2.png");
      
    } else if (this.screenNum == 2){
      this.firstAction = true;
      this.startTimer = true;
      curImage = new FromFileImage("MerchantScreen.png");
      
    } else if (this.screenNum == 3){
      
      curImage = new FromFileImage("InstructionsScreen.png");
      
    } else if (this.screenNum == 4) {
      
      this.firstAction = true;
      WorldImage instrImg = new FromFileImage("ShiftInstructions.png");
      WorldImage moneyText  = new TextImage("Total Money: " + this.totalMoney, 30, new Color(139, 128,0));
      WorldImage combinedImage = new AboveImage(instrImg, moneyText);
      WorldImage smallGold = new FromFileImage("Goldflake.png");
      s.placeImageXY(combinedImage, this.width - (int)combinedImage.getWidth()/2, (int)combinedImage.getHeight()/2);
      curImage = new FromFileImage("Panning.png");
      
      
      if(this.startSifting && this.timer >= 4) {
        //kill after a few animations
        this.startTimer = false;
        this.timer = 0;
        this.startSifting = false;
        if((int)(Math.random() * 10) + this.numCycleSifting <= 5) {
          System.out.println("GOT GOLD");
          this.gotGold = true;
          this.numCycleSifting += 1;
          this.money += 2;
          this.totalMoney += 2;
        }
        
      } else if(this.startSifting && this.timer % 2 == 0) {
       
        s.placeImageXY(curImage, this.widthXY + 10, this.heightXY);
        return s;
        
      } else if(this.startSifting && this.timer % 2 == 1) {
        
        s.placeImageXY(curImage, this.widthXY - 40, this.heightXY);
        return s;
      }
      
      if(this.actionValue > 40) {
        s.placeImageXY(curImage, this.widthXY, this.heightXY);
        WorldImage nightMsg = new TextImage("It's night please go to sleep", 30, Color.black);
        s.placeImageXY(nightMsg, (int) nightMsg.getWidth()/2 + 30, (int) nightMsg.getHeight()/2);
        this.gotGold = false;
        return s;
      }
      
      if(this.gotGold) {
        s.placeImageXY(curImage, this.widthXY, this.heightXY);
        s.placeImageXY(smallGold, this.widthXY, this.heightXY);
        WorldImage goldMsg = new TextImage("Congrats on Finding Gold!", 30, new Color(139, 128,0));
        s.placeImageXY(goldMsg, (int) goldMsg.getWidth()/2 + 30, (int) goldMsg.getHeight()/2);
        return s;
      } else if (this.numCycleSifting > 5) {
        s.placeImageXY(curImage, this.widthXY, this.heightXY);
        WorldImage goldMsg = new TextImage("No more Gold to Find!", 30, Color.black);
        WorldImage newBatch = new TextImage("Switch to a new Batch to Find more Gold!", 30, Color.black);
        WorldImage combinedMsg = new AboveImage(goldMsg, newBatch);
        s.placeImageXY(combinedMsg, (int) combinedMsg.getWidth()/2 + 10, (int) combinedMsg.getHeight()/2 + 20);
        return s;
      }
      

      
    } else if (this.screenNum == 5) {
      this.onKeyEvent(".....");
      
      if(this.hp <= 0) { 
        this.endOfWorld("You have succumbed to your injuries and have died.");
        return this.lastScene("You have succumbed to your injuries and have died.");
      }
      
      if(this.phungryCount == 3) {
        
        this.endOfWorld("You haven't eaten in days! You're too weak to work and have died.");
        return this.lastScene("You haven't eaten in days! You're too weak to work and have died.");
      }
      
      if(this.fhungryCount == 3) {
        this.endOfWorld("You didn't send money back to your Family and they starved to death.");
        return this.lastScene("You didn't send money back to your Family and they starved to death.");
      }
      
      if(this.merchantDebt == 0 && this.totalMoney >= 500) {
        this.win = true;
        this.endOfWorld("YOU'VE OPENED YOUR OWN BUSSINESS");
        return this.lastScene("YOU'VE OPENED YOUR OWN BUSSINESS");
      }
      WorldImage top = new TextImage("End of Day Summary", 15, Color.black);
      WorldImage goals = new TextImage("Goals", 15, Color.green);
      
      WorldImage moneyEarned = new TextImage("Money Earned Today: $" + this.money, 15, new Color(139, 128,0));
      
      WorldImage merchantDebt = new TextImage("Merchant's Share: -$2", 15, Color.red );
      if(this.merchantDebt == 0) {
        merchantDebt = new TextImage("Merchant's Share: $0", 15, Color.green );
      }
      
      if(!this.paidMerchant) {
        merchantDebt = new TextImage("Merchant wasn't paid today! Be careful at night!", 15, Color.red);
      } 
      
      WorldImage merchantDebtLeft = new TextImage("Remaining Debt: $" + this.merchantDebt, 15, Color.red);
      if (this.merchantDebt == 0) {
        merchantDebtLeft = new TextImage("Remaining Debt: $" + this.merchantDebt, 15, Color.green);
      }
      
      WorldImage moneySentHome = new TextImage("Money Sent Home: -$2", 15, Color.blue );
      if(!this.paidFamily) {
        moneySentHome = new TextImage("No money was sent home today! Don't let your family starve", 15, Color.red);
      }
      
      WorldImage foodExpense = new TextImage("Cost of Food: -$2", 15, Color.red);
      if(!this.paidFood) {
        foodExpense = new TextImage("Can't afford Food Today!", 15, Color.red);
      }

      WorldImage blank = new RectangleImage(30, 30, OutlineMode.OUTLINE, Color.white);

      WorldImage sumImages = new AboveImage(top,blank,moneyEarned, blank,merchantDebt, blank,moneySentHome, blank, foodExpense );
      
      if(this.days % 30 == 0 && this.days != 0) {
        WorldImage stateTax = new TextImage("State Tax for Foreign Miners: -$20", 15, Color.red );
        if(days > 60) {
          stateTax = new TextImage("State Tax for Foreign Miners: -$4", 15, Color.red );
        }
        sumImages = new AboveImage(sumImages, blank, blank, stateTax);
      }
      
      
      WorldImage moneyTotal = new TextImage("Total Money: $" + this.totalMoney, 15, new Color(139, 128,0));
      WorldImage endGoal = new TextImage("Open Business: $500", 15, Color.red);
      if(this.totalMoney >= 1000) {
        endGoal = new TextImage("Open Business: $500", 15, Color.green);
      }
      
      WorldImage goalImages = new AboveImage(goals, blank, merchantDebtLeft, blank, endGoal, blank, moneyTotal );
      
      curImage = new BesideImage(sumImages, goalImages);
      
      
    } else if(this.screenNum == 6) {
      if(this.jumped && !this.paidMerchant) {
        curImage = new FromFileImage("OweMerchant.png");
      } else if(this.jumped && (this.paidMerchant && this.bMerchant && this.merchantDebt > 0)) {
        curImage = new FromFileImage("Protected.png");
      } else if(this.jumped){
        curImage = new FromFileImage("RacialViolence.png");
      }
    }
    
    s.placeImageXY(curImage, this.widthXY, this.heightXY);
    
    return s;
  }
  
  public WorldScene lastScene(String msg) {
    WorldScene s = new WorldScene( this.width, this.height);
    WorldImage lastMsg = new TextImage(msg, 15, Color.red);
    WorldImage deathMsg = new TextImage("You Lose", 30, Color.red);
    if(this.win) {
      lastMsg = new TextImage(msg, 30, Color.green);
      deathMsg = new TextImage("YOU WIN", 30, Color.green);
    } 
    
    WorldImage combinedMsg = new AboveImage(lastMsg, deathMsg);
    s.placeImageXY(combinedMsg,this.widthXY, this.heightXY);
    
    return s;
  }
  
}

class ExampleGame {
  
  void testBigBang(Tester t) {
    ImmigrationGame ig = new ImmigrationGame();
    ig.bigBang(1000, 1000, .5);
  }
  
}