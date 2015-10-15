//
//  SignInScreenViewController.m
//  StudentTracker
//
//  Created by AppKnetics on 21/05/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import "SignInScreenViewController.h"
#import "CurrentAdressScreenViewController.h"
#import "HomeScreenViewController.h"
#import "ContactUsViewController.h"
#import "PintCurrentLocationViewController.h"
#import "AccessViewController.h"

@interface SignInScreenViewController ()

@end

@implementation SignInScreenViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.phoneTxt.borderStyle = UITextBorderStyleNone;
    self.phoneTxt.layer.borderWidth = 0.0f ;
    
    UIView *paddingView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 5, 20)];
    self.phoneTxt.leftView = paddingView;
    self.phoneTxt.leftViewMode = UITextFieldViewModeAlways;
    
    UITapGestureRecognizer *tap=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(tappclick)];
    tap.numberOfTapsRequired=1;
    [self.view addGestureRecognizer:tap];
    
    NSArray *viewControllers = self.navigationController.viewControllers;
    if([viewControllers count] > 1) {
        [self.backImg setHidden:NO] ;
        [self.btnObj setHidden:NO] ;
    }else{
        [self.backImg setHidden:YES] ;
        [self.btnObj setHidden:YES] ;
    }
    
    // Do any additional setup after loading the view from its nib.
}


-(void)tappclick
{
    [_phoneTxt resignFirstResponder];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)signinclicked:(id)sender {
    
    
    if (!(self.phoneTxt.text.length >0)) {
        UIAlertView *alert=[[UIAlertView alloc]initWithTitle:AppName message:@"Please enter the phone number" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alert show];
    }else{
        NSLog(@"signin clicked");
        
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        NSString *URLString =[kBaseURL stringByAppendingString:authentication];
        NSLog(@"URL=%@",URLString);
        
        AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
        NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:self.phoneTxt.text,@"phone", nil];
        NSLog(@"parmeters=%@",params);
        
        [manager GET:URLString  parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
            
            NSLog(@"responseobject=%@",responseObject);
            if([responseObject isKindOfClass:[NSDictionary class]]) {
                NSString *status=[responseObject valueForKey:@"status"] ;
                if ([status intValue]==1) {
                    [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                    NSDictionary *dataVal = [responseObject objectForKey:@"data"];
                   /* studentLoginAlert=[[UIAlertView alloc]initWithTitle:AppName message:[dataVal valueForKey:@"message"] delegate:self cancelButtonTitle:@"Yes" otherButtonTitles:@"No", nil];
                    [studentLoginAlert show];*/
                    AccessViewController *accessView = [[AccessViewController alloc] init];
                    accessView.phoneNumberVal = self.phoneTxt.text ;
                    [self.navigationController pushViewController:accessView animated:YES] ;
                    
                }else{
                    [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                    hud.mode = MBProgressHUDModeText;
                    hud.detailsLabelText = @"”Entered phone number desn't match with our system. If you have any queries please contact to school by clicking on above Contact Us link";
                    hud.margin = 10.f;
                    hud.yOffset = 200.f;
                    hud.removeFromSuperViewOnHide = YES;
                    [hud hide:YES afterDelay:7];
                }
            }else{
                [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                hud.mode = MBProgressHUDModeText;
                hud.detailsLabelText = @"”Sign in failed. Please try again with correct phone number.”";
                hud.margin = 10.f;
                hud.yOffset = 200.f;
                hud.removeFromSuperViewOnHide = YES;
                [hud hide:YES afterDelay:2];
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

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(alertView == studentLoginAlert){
        if(buttonIndex == 0) {
            [self confirmStudent] ;
        }
    }
}

-(void) confirmStudent {
    [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    NSString *URLString =[kBaseURL stringByAppendingString:confirmauth];
    NSLog(@"URL=%@",URLString);
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:self.phoneTxt.text,@"phone",appDelegate.deviceToken,@"push_id", @"ios", @"device_family", nil];
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
                [hud hide:YES afterDelay:2];
                NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
                if([[responseObject valueForKey:@"data"] isKindOfClass:[NSDictionary class]]) {
                    [userDefault setValue:[[responseObject valueForKey:@"data"] valueForKey:@"id"] forKey:@"student_id"] ;
                    [userDefault setValue:[[responseObject valueForKey:@"data"] valueForKey:@"name"] forKey:@"student_name"] ;
                    [userDefault setValue:self.phoneTxt.text forKey:@"student_phone"] ;
                    
                    [userDefault setObject:[[responseObject valueForKey:@"data"] valueForKey:@"user_latitude"] forKey:@"userLatitude"];
                    [userDefault setObject:[[responseObject valueForKey:@"data"] valueForKey:@"user_longitude"] forKey:@"userLongitude"];
                    
                }
                
                HomeScreenViewController *homeView=[[HomeScreenViewController alloc]init];
                [self.navigationController pushViewController:homeView animated:YES];
                
                /*if([userDefault valueForKey:@"pinlocation"] && [[userDefault valueForKey:@"pinlocation"] length] > 0) {
                    HomeScreenViewController *homeView=[[HomeScreenViewController alloc]init];
                    [self.navigationController pushViewController:homeView animated:YES];
                }else{
                    PintCurrentLocationViewController *pinView = [[PintCurrentLocationViewController alloc] init];
                    
                    
                    
                    [self.navigationController pushViewController:pinView animated:YES] ;
                }*/
                
                
            }else{
                [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                hud.mode = MBProgressHUDModeText;
                hud.labelText = @"”Sign in failed. Please try again”";
                hud.margin = 10.f;
                hud.yOffset = 200.f;
                hud.removeFromSuperViewOnHide = YES;
                [hud hide:YES afterDelay:2];
            }
        }else{
            [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
            MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
            hud.mode = MBProgressHUDModeText;
            hud.detailsLabelText = @"”Sign in failed. Please try again with correct phone number.”";
            hud.margin = 10.f;
            hud.yOffset = 200.f;
            hud.removeFromSuperViewOnHide = YES;
            [hud hide:YES afterDelay:2];
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

- (IBAction)forgetpassclicked:(id)sender {
    NSLog(@"forgetpass clicked");
}
-(void)viewWillAppear:(BOOL)animated
{
    [self.navigationController setNavigationBarHidden:YES];
}
-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}

-(void) textFieldDidBeginEditing:(UITextField *)textField {
    currentTxt = textField ;
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

-(IBAction)contactUsView:(id)sender {
    ContactUsViewController *contactView = [[ContactUsViewController alloc] init];
    contactView.redirectionType = @"1";
    [self presentViewController:contactView animated:YES completion:nil];
   // [self.navigationController pushViewController:contactView animated:YES];
}

-(void) resignKeyboard {
    [currentTxt resignFirstResponder] ;
}

-(IBAction)backView:(id)sender {
    [self.navigationController popViewControllerAnimated:YES] ;
}

-(void) previousTextField {
    
}

-(void) nextTextField {
    
}

@end
