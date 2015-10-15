//
//  AccessViewController.h
//  StudentTracker
//
//  Created by Umesh Dhuri on 9/23/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AccessViewController : UIViewController <UITextFieldDelegate> {
    UITextField *currentTxt ;
    BOOL moved ;
}

@property (nonatomic, weak) IBOutlet UITextField *codeTxt ;
@property (nonatomic, weak) IBOutlet UIButton *contnueBtn, *resendBtn ;
@property (nonatomic, strong) NSString *phoneNumberVal ;
@end
