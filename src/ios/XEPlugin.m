//
//  XEPlugin.m
//  ios
//
//  Created by ywen on 15/7/1.
//  Copyright (c) 2015年 ywen. All rights reserved.
//

#import "XEPlugin.h"

@implementation XEPlugin

-(void)normalCommand:(CDVInvokedUrlCommand *)command {
#ifdef DEBUG
    NSLog(@"command is %@", command);
#endif
    
    NSString *arg1;
    @try {
        arg1 = [command.arguments objectAtIndex:0];
    }
    @catch (NSException *exception) {
        NSLog(@"error: %@", exception);
    }
    @finally {
        
    }
    
    if ([self.viewController conformsToProtocol: @protocol(XEProtocol)]) {
        id <XEProtocol>  xe = (id <XEProtocol>)self.viewController;
        switch ([arg1 integerValue]) {
            case NavNext:
            {
                [xe navNext];
                break;
            }
                
            case NavBack:
            {
                [xe navBack];
                break;
            }
                
            default:
            {
                [xe commonCommand:command.arguments];
                break;
            }
               
        }
    }
}

@end
