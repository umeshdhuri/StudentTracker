//
//  ContactUsViewController.h
//  StudentTracker
//
//  Created by Umesh Dhuri on 6/10/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ContactUsViewController : UIViewController <CCKFNavDrawerDelegate, UITextViewDelegate, UITextFieldDelegate> {
    UITextField *currentTxt ;
    UITextView *currentTextView ;
    BOOL moved ;
}

- (IBAction)drawerclicked:(id)sender;
@property (nonatomic, strong) NSString *redirectionType ;
@property (nonatomic, strong) NSString *addressVal ;
@property (nonatomic, weak) IBOutlet UIImageView *backImg;
@property (nonatomic, weak) IBOutlet UIButton *backBtn ;
@property (nonatomic, weak) IBOutlet UIView *topView ;
@property (strong, nonatomic) sliderDrawe1ViewController *rootNav;
@property (nonatomic, weak) IBOutlet UITextField *nameTxt, *phoneTxt, *hideTxt ;
@property (nonatomic, weak) IBOutlet UITextView *reasonTxt ;
@property (nonatomic, weak) IBOutlet UITextField *titleTxt ;
@property (nonatomic, weak) IBOutlet UIView *landingView;

@end
