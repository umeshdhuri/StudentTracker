//
//  ContactUsViewController.m
//  StudentTracker
//
//  Created by Umesh Dhuri on 6/10/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import "ContactUsViewController.h"

@interface ContactUsViewController ()

@end

@implementation ContactUsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    moved = false ;
    NSUserDefaults *userDefaultVal = [NSUserDefaults standardUserDefaults];
    if([self.redirectionType isEqualToString:@"1"]) {
        [self.topView setHidden:YES];
        [self.backBtn setHidden:NO] ;
        [self.backImg setHidden:NO] ;
        
    }else if([self.redirectionType isEqualToString:@"2"]) {
        self.rootNav = (sliderDrawe1ViewController *)self.navigationController;
        [self.rootNav setCCKFNavDrawerDelegate:self];
        [self.topView setHidden:NO];
        [self.backBtn setHidden:YES] ;
        [self.backImg setHidden:YES] ;
        [self.titleTxt setText:@"Change Address Request"];
        [self.reasonTxt setText:self.addressVal] ;
        
        [self.nameTxt setText:[userDefaultVal valueForKey:@"student_name"]] ;
        [self.phoneTxt setText:[userDefaultVal valueForKey:@"student_phone"]] ;
        
    }else{
        self.rootNav = (sliderDrawe1ViewController *)self.navigationController;
        [self.rootNav setCCKFNavDrawerDelegate:self];
        [self.topView setHidden:NO];
        [self.backBtn setHidden:YES] ;
        [self.backImg setHidden:YES] ;
        
        [self.nameTxt setText:[userDefaultVal valueForKey:@"student_name"]] ;
        [self.phoneTxt setText:[userDefaultVal valueForKey:@"student_phone"]] ;
    }
    
    self.reasonTxt.layer.borderWidth = 1 ;
    [self.reasonTxt.layer setBorderColor:[[[UIColor blackColor] colorWithAlphaComponent:0.5] CGColor]];
    [self addDoneToolBarToKeyboard:self.reasonTxt];
    // Do any additional setup after loading the view from its nib.
}

- (IBAction)drawerclicked:(id)sender {
    [self.rootNav drawerToggle];
    
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}

-(void) textFieldDidBeginEditing:(UITextField *)textField {
    currentTxt = textField ;
    if(textField != self.nameTxt) {
        if(!moved) {
            [self animateViewToPosition:self.landingView directionUP:YES textfield:textField];
            moved = YES;
        }
    }
    [currentTextView resignFirstResponder] ;
}

-(void)textFieldDidEndEditing:(UITextField *)sender
{
    UITextField *txtFieldObj = (UITextField *) sender ;
    if(txtFieldObj != self.nameTxt) {
        if(moved) {
            [self animateViewToPosition:self.landingView directionUP:NO textfield:txtFieldObj];
        }
        moved = NO;
    }
}

-(void) resignKeyboard {
    [currentTxt resignFirstResponder] ;
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

- (BOOL)textViewShouldBeginEditing:(UITextView *)textView{
    NSLog(@"textViewShouldBeginEditing:");
    
    return YES;
}

- (void)textViewDidBeginEditing:(UITextView *)textView {
    NSLog(@"textViewDidBeginEditing:");
    currentTextView = textView;
    if([textView.text isEqualToString:@"Enter Your Queries"]) {
        textView.text = @"";
    }else{
        
    }
    
    if(!moved) {
        [self animateViewToPosition:self.landingView directionUP:YES textfield:self.hideTxt];
        moved = YES;
    }
}

-(void) textViewDidEndEditing:(UITextView *)textView {
    if([[textView.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] length] <= 0) {
        textView.text = @"Enter Your Queries";
    }
    
    if(moved) {
        [self animateViewToPosition:self.landingView directionUP:NO textfield:self.hideTxt];
    }
    moved = NO;
}

-(void)addDoneToolBarToKeyboard:(UITextView *)textView
{
    UIToolbar * doneToolbar = [[UIToolbar alloc] initWithFrame:CGRectMake(0, 0, 320, 50)];
    
    doneToolbar.barStyle = UIBarStyleDefault;
    [doneToolbar setItems: [NSArray arrayWithObjects:
                                [[UIBarButtonItem alloc]initWithTitle:@"" style:UIBarButtonItemStyleBordered target:self action:@selector(previousTextField)],
                                
                                [[UIBarButtonItem alloc] initWithTitle:@"" style:UIBarButtonItemStyleBordered target:self action:@selector(nextTextField)],
                                [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil],
                                [[UIBarButtonItem alloc]initWithTitle:@"Done" style:UIBarButtonItemStyleDone target:self action:@selector(doneButtonClickedDismissKeyboard)],
                                nil]];
    textView.inputAccessoryView = doneToolbar;
}

-(void)doneButtonClickedDismissKeyboard
{
    [currentTextView resignFirstResponder];
}

-(IBAction) backView:(id)sender {
    //[self.navigationController popViewControllerAnimated:YES] ;
    [self dismissViewControllerAnimated:YES completion:nil] ;
}

-(void) previousTextField {
    
}

-(void) nextTextField {
    
}

-(IBAction)contactUs:(id)sender {
    if([[self.nameTxt.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] length] <= 0) {
        UIAlertView *errMsg = [[UIAlertView alloc] initWithTitle:AppName message:@"Please enter name." delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [errMsg show] ;
    }else if([[self.phoneTxt.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] length] <= 0){
        UIAlertView *errMsg = [[UIAlertView alloc] initWithTitle:AppName message:@"Please enter phone number." delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [errMsg show] ;
    }else if([[self.titleTxt.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] length] <= 0){
        UIAlertView *errMsg = [[UIAlertView alloc] initWithTitle:AppName message:@"Please enter contact us title." delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [errMsg show] ;
    }else if([[self.reasonTxt.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] length] <= 0){
        UIAlertView *errMsg = [[UIAlertView alloc] initWithTitle:AppName message:@"Please enter you queries." delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [errMsg show] ;
    }else{
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        NSString *URLString =[kBaseURL stringByAppendingString:contactus];
        NSLog(@"URL=%@",URLString);
        
        AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
        NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:self.nameTxt.text,@"name",self.phoneTxt.text,@"phone", self.titleTxt.text, @"title",self.reasonTxt.text, @"query", nil];
        NSLog(@"parmeters=%@",params);
        
        [manager POST:URLString  parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
            
            NSLog(@"responseobject=%@",responseObject);
            if([responseObject isKindOfClass:[NSDictionary class]]) {
                NSString *status=[responseObject valueForKey:@"status"] ;
                if ([status intValue]==1) {
                    [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                    hud.mode = MBProgressHUDModeText;
                    hud.detailsLabelText = @"”Contact us request sent Successfully. Administrator will contact you shortly.”";
                    hud.margin = 10.f;
                    hud.yOffset = 200.f;
                    hud.removeFromSuperViewOnHide = YES;
                    [hud hide:YES afterDelay:3];
                    
                    [self.titleTxt setText:@""] ;
                    [self.reasonTxt setText:@"Enter Your Queries"] ;
                    
                }else{
                    [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                    hud.mode = MBProgressHUDModeText;
                    hud.detailsLabelText = @"”Some error occure. Please try again”";
                    hud.margin = 10.f;
                    hud.yOffset = 200.f;
                    hud.removeFromSuperViewOnHide = YES;
                    [hud hide:YES afterDelay:3];
                    
                }
            }else{
                [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                hud.mode = MBProgressHUDModeText;
                hud.detailsLabelText = @"”Some error occure. Please try again”";
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

#pragma mark -
#pragma mark Keypad Animation
-(void)animateViewToPosition:(UIView *)viewToMove directionUP:(BOOL)up textfield:(UITextField *)textObjVal {
    
    int movementDistance;
    
    if(textObjVal == self.phoneTxt) {
        movementDistance = -50; // tweak as needed
    }else if(textObjVal == self.titleTxt) {
        movementDistance = -60; // tweak as needed
    }else if(textObjVal == self.hideTxt) {
        if(isPhone480) {
            movementDistance = -150; // tweak as needed
        }else{
            movementDistance = -110; // tweak as needed
        }
        
    }else{
        movementDistance = -10; // tweak as needed
    }
    const float movementDuration = 0.3f; // tweak as needed
    
    int movement = (up ? movementDistance : -movementDistance);
    [UIView beginAnimations: @"animateTextField" context: nil];
    [UIView setAnimationBeginsFromCurrentState: YES];
    [UIView setAnimationDuration: movementDuration];
    viewToMove.frame = CGRectOffset(viewToMove.frame, 0, movement);
    [UIView commitAnimations];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
