//
//  AccessViewController.m
//  StudentTracker
//
//  Created by Umesh Dhuri on 9/23/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import "AccessViewController.h"
#import "HomeScreenViewController.h"
@interface AccessViewController ()

@end


@implementation AccessViewController
@synthesize phoneNumberVal ;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    moved = false ;
    self.codeTxt.borderStyle = UITextBorderStyleNone;
    self.codeTxt.layer.borderWidth = 0.0f ;
    
    UIView *paddingView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 5, 20)];
    self.codeTxt.leftView = paddingView;
    self.codeTxt.leftViewMode = UITextFieldViewModeAlways;
    
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(BOOL) textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder] ;
    return YES ;
}

-(void) textFieldDidBeginEditing:(UITextField *)textField {
    currentTxt = textField ;
    if(isPhone480) {
        if(!moved) {
            [self animateViewToPosition:self.view directionUP:YES textfield:textField];
            moved = YES;
        }
    }
}

-(void)textFieldDidEndEditing:(UITextField *)sender
{
    UITextField *txtFieldObj = (UITextField *) sender ;
    if(isPhone480) {
        if(moved) {
            [self animateViewToPosition:self.view directionUP:NO textfield:txtFieldObj];
        }
        moved = NO;
    }
}

-(IBAction)continueBtnClick:(id)sender {
    [self.codeTxt resignFirstResponder] ;
    if (!(self.codeTxt.text.length > 0)) {
        UIAlertView *alert=[[UIAlertView alloc]initWithTitle:AppName message:@"Please enter the code" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alert show];
    }else{
        
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        NSString *URLString =[kBaseURL stringByAppendingString:confirmauth];
        NSLog(@"URL=%@",URLString);
        
        AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
        NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:phoneNumberVal,@"phone", self.codeTxt.text,@"sms_code", appDelegate.deviceToken,@"push_id", @"ios", @"device_family", nil];
        NSLog(@"parmeters=%@",params);
        
        [manager GET:URLString  parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
            
            NSLog(@"responseobject=%@",responseObject);
            if([responseObject isKindOfClass:[NSDictionary class]]) {
                NSString *status=[responseObject valueForKey:@"status"] ;
                if ([status intValue]==1) {
                    [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                    hud.mode = MBProgressHUDModeText;
                    hud.labelText = @"”Sign in successfully.”";
                    hud.margin = 10.f;
                    hud.yOffset = 200.f;
                    hud.removeFromSuperViewOnHide = YES;
                    [hud hide:YES afterDelay:3];
                    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
                    if([[responseObject valueForKey:@"data"] isKindOfClass:[NSDictionary class]]) {
                        [userDefault setValue:[[responseObject valueForKey:@"data"] valueForKey:@"id"] forKey:@"student_id"] ;
                        [userDefault setValue:[[responseObject valueForKey:@"data"] valueForKey:@"name"] forKey:@"student_name"] ;
                        [userDefault setValue:phoneNumberVal forKey:@"student_phone"] ;
                        
                        [userDefault setObject:[[responseObject valueForKey:@"data"] valueForKey:@"user_latitude"] forKey:@"userLatitude"];
                        [userDefault setObject:[[responseObject valueForKey:@"data"] valueForKey:@"user_longitude"] forKey:@"userLongitude"];
                        
                    }
                    
                    HomeScreenViewController *homeView=[[HomeScreenViewController alloc]init];
                    [self.navigationController pushViewController:homeView animated:YES];
                    
                }else{
                    [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                    hud.mode = MBProgressHUDModeText;
                    hud.detailsLabelText = @"”Sign in failed. Please enter correct code number which you received by sms.”";
                    hud.margin = 10.f;
                    hud.yOffset = 200.f;
                    hud.removeFromSuperViewOnHide = YES;
                    [hud hide:YES afterDelay:3];
                    
                }
            }else{
                [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                hud.mode = MBProgressHUDModeText;
                hud.detailsLabelText = @"”Sign in failed. Please enter correct code number which you received by sms.”";
                hud.margin = 10.f;
                hud.yOffset = 200.f;
                hud.removeFromSuperViewOnHide = YES;
                [hud hide:YES afterDelay:3];
            }
            
            
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
            NSLog(@"Error: %@", error);
            NSString *errmsg=[error.userInfo valueForKey:@"NSLocalizedDescription"];
            UIAlertView *connectionErrMsg = [[UIAlertView alloc] initWithTitle:AppName message:errmsg delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
            //_nocarlbl.text=errmsg;
            [connectionErrMsg show];
            
        }];
    }
}

- (IBAction)resendSmsCode:(id)sender {
    
    [self.codeTxt resignFirstResponder] ;
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        NSString *URLString =[kBaseURL stringByAppendingString:authentication];
        NSLog(@"URL=%@",URLString);
        
        AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
        NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:phoneNumberVal,@"phone", nil];
        NSLog(@"parmeters=%@",params);
        
        [manager GET:URLString  parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
            
            NSLog(@"responseobject=%@",responseObject);
            if([responseObject isKindOfClass:[NSDictionary class]]) {
                NSString *status=[responseObject valueForKey:@"status"] ;
                if ([status intValue]==1) {
                    [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                    hud.mode = MBProgressHUDModeText;
                    hud.labelText = @"”Resend code successfully.”";
                    hud.margin = 10.f;
                    hud.yOffset = 200.f;
                    hud.removeFromSuperViewOnHide = YES;
                    [hud hide:YES afterDelay:4];
                    
                }else{
                    [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                    hud.mode = MBProgressHUDModeText;
                    hud.detailsLabelText = @"”Some error occurs. Please try again.";
                    hud.margin = 10.f;
                    hud.yOffset = 200.f;
                    hud.removeFromSuperViewOnHide = YES;
                    [hud hide:YES afterDelay:4];
                }
            }else{
                [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                hud.mode = MBProgressHUDModeText;
                hud.detailsLabelText = @"”Some error occurs. Please try again.”";
                hud.margin = 10.f;
                hud.yOffset = 200.f;
                hud.removeFromSuperViewOnHide = YES;
                [hud hide:YES afterDelay:4];
            }
            
            
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
            NSLog(@"Error: %@", error);
            NSString *errmsg=[error.userInfo valueForKey:@"NSLocalizedDescription"];
            UIAlertView *connectionErrMsg = [[UIAlertView alloc] initWithTitle:AppName message:errmsg delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
            //_nocarlbl.text=errmsg;
            [connectionErrMsg show];
            
        }];
    
}


-(BOOL)textFieldShouldBeginEditing:(UITextField *)textField {
    UIToolbar * keyboardToolBar = [[UIToolbar alloc] initWithFrame:CGRectMake(0, 0, 320, 50)];
    
    keyboardToolBar.barStyle = UIBarStyleDefault;
    [keyboardToolBar setItems: [NSArray arrayWithObjects:
                                [[UIBarButtonItem alloc]initWithTitle:@"" style:UIBarButtonItemStyleBordered target:self action:@selector(previousTextField)],
                                
                                [[UIBarButtonItem alloc] initWithTitle:@"" style:UIBarButtonItemStyleBordered target:self action:@selector(nextTextField)],
                                [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil],
                                [[UIBarButtonItem alloc]initWithTitle:@"Done" style:UIBarButtonItemStyleDone target:self action:@selector(resignKeyboard)],
                                nil]];
    textField.inputAccessoryView = keyboardToolBar;
    
    return YES;
}

-(void) previousTextField {
    
}

-(void) nextTextField {
    
}

-(void) resignKeyboard {
    [currentTxt resignFirstResponder] ;
}

-(IBAction)backView:(id)sender {
    [self.navigationController popViewControllerAnimated:YES] ;
}


#pragma mark -
#pragma mark Keypad Animation
-(void)animateViewToPosition:(UIView *)viewToMove directionUP:(BOOL)up textfield:(UITextField *)textObjVal {
    
    int movementDistance;
    
   movementDistance = -40; // tweak as needed
    const float movementDuration = 0.3f; // tweak as needed
    
    int movement = (up ? movementDistance : -movementDistance);
    [UIView beginAnimations: @"animateTextField" context: nil];
    [UIView setAnimationBeginsFromCurrentState: YES];
    [UIView setAnimationDuration: movementDuration];
    viewToMove.frame = CGRectOffset(viewToMove.frame, 0, movement);
    [UIView commitAnimations];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
