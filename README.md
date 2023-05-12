# AdModFrameWork
**Step 1**: Add to your build file:

    allprojects {
    
        repositories {
        
            ...
            
            maven { url 'https://jitpack.io' }
            
        }
        
    }
    
    ...
    
    dependencies {
    
        implementation 'com.github.ThanhViet91:AdModFrameWork:1.3.7'
        
    }

**Step 2**: Using Ads SDK:
1. InitialAdMod SDK:

    AdsUtil.initialAdMod();
   
2. Create Ads:

    AdsUtil.createBanner(context, id, containerAdView);
    
    AdsUtil.createNativeAd(context, id, containerAdView);
    ...
