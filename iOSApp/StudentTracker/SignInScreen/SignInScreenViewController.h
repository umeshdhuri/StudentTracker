//
//  SignInScreenViewController.h
//  StudentTracker
//
//  Created by AppKnetics on 21/05/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SignInScreenViewController : UIViewController<UITextFieldDelegate> {
    UITextField *currentTxt ;
    UIAlertView *studentLoginAlert ;
}


- (IBAction)signinclicked:(id)sender;

- (IBAction)forgetpassclicked:(id)sender;

@property (weak, nonatomic) IBOutlet UITextField *phoneTxt;
@property (nonatomic, weak) IBOutlet UIImageView *backImg ;
@property (nonatomic, weak) IBOutlet UIButton *btnObj ;

@end
